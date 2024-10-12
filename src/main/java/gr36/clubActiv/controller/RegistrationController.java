package gr36.clubActiv.controller;

import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.Response;
import gr36.clubActiv.exeption_handling.exeptions.UserAlreadyExistsException;
import gr36.clubActiv.services.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/register")
public class RegistrationController {

  private final UserService service;

  public RegistrationController(UserService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<Response> register(@Valid @RequestBody User user) {
    try {
      service.register(user);
      return ResponseEntity.ok(
          new Response("Registration complete. Please check your email.", 200));
    } catch (UserAlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(new Response("Registration failed: " + e.getMessage(), 409));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new Response("Registration failed: " + e.getMessage(), 400));
    }
  }

  @GetMapping
  public ResponseEntity<Response> registrationConfirm(@RequestParam String code) {
    try {
      service.registrationConfirm(code);
      return ResponseEntity.ok(new Response("Registration confirmed successfully", 200));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new Response("Confirmation failed: " + e.getMessage(), 400));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new Response("An error occurred: " + e.getMessage(), 500));
    }
  }
}
