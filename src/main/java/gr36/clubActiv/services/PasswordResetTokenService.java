package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.PasswordResetToken;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.repository.PasswordResetTokenRepository;
import jakarta.persistence.Transient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetTokenService {

  private final PasswordResetTokenRepository tokenRepository;

  public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  public String createPasswordResetTokenForUser(User user) {
    String token = UUID.randomUUID().toString();
    PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusHours(24));  // Valid for 24 hours
    tokenRepository.save(resetToken);
    return token;
  }

  public Optional<PasswordResetToken> validatePasswordResetToken(String token) {
    return tokenRepository.findByToken(token)
        .filter(resetToken -> resetToken.getExpirationTime().isAfter(LocalDateTime.now()));
  }
@Transactional
  public void deletePasswordResetToken(String token) {
    tokenRepository.deleteByToken(token);
  }
}
