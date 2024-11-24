package gr36.clubActiv.controller;

import gr36.clubActiv.domain.entity.PasswordResetToken;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.services.PasswordResetTokenService;
import gr36.clubActiv.services.interfaces.EmailService;
import gr36.clubActiv.services.interfaces.UserService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PasswordResetController {

  private final UserService userService;
  private final EmailService emailService;
  private final PasswordResetTokenService passwordResetTokenService;

  public PasswordResetController(UserService userService, EmailService emailService, PasswordResetTokenService passwordResetTokenService) {
    this.userService = userService;
    this.emailService = emailService;
    this.passwordResetTokenService = passwordResetTokenService;
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
    Optional<User> user = userService.findByEmail(email);
    if (user.isPresent()) {

      String token = passwordResetTokenService.createPasswordResetTokenForUser(user.get());
      String resetUrl = "http://localhost:5173/#/reset-password?token=" + token;


      emailService.sendPasswordResetEmail(user.get().getEmail(), resetUrl);
      return ResponseEntity.ok("Password reset email sent.");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found.");
  }

  @GetMapping("/validate-reset-token")
  public ResponseEntity<String> validateToken(@RequestParam String token) {
    Optional<PasswordResetToken> resetToken = passwordResetTokenService.validatePasswordResetToken(token);
    if (resetToken.isPresent()) {
      return ResponseEntity.ok("Token is valid");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
    }
  }

  @PutMapping("/reset-password")
  public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
    Optional<PasswordResetToken> resetToken = passwordResetTokenService.validatePasswordResetToken(token);

    if (resetToken.isPresent()) {
      User user = resetToken.get().getUser();
      userService.updatePassword(user, newPassword);
      passwordResetTokenService.deletePasswordResetToken(token);
      return ResponseEntity.ok("Password updated successfully");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
    }
  }
}
