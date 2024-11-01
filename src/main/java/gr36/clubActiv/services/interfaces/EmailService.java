package gr36.clubActiv.services.interfaces;

import gr36.clubActiv.domain.entity.User;

public interface EmailService {

  void sendConfirmationEmail(User user);

  void sendPasswordResetEmail(String email, String resetUrl);
}
