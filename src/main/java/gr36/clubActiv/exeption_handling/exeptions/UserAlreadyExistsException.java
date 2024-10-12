package gr36.clubActiv.exeption_handling.exeptions;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException(String message) {
    super(message); // Passes the message to the parent class (RuntimeException)
  }
}
