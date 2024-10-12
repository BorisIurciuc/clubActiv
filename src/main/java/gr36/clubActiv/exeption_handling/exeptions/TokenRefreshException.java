package gr36.clubActiv.exeption_handling.exeptions;

public class TokenRefreshException extends RuntimeException {
  public TokenRefreshException(String message) {
    super(message);
  }
}