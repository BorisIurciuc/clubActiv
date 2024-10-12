package gr36.clubActiv.exeption_handling.exeptions;


public class AuthenticationFailedException extends RuntimeException {
  public AuthenticationFailedException(String message) {
    super(message);
  }
}

