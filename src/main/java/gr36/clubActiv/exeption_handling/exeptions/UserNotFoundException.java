package gr36.clubActiv.exeption_handling.exeptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(Long userId) {
    super(String.format("User with id %d not found", userId));
  }

  public UserNotFoundException(String message) {
    super(message); // Используем переданное сообщение, как и должно быть
  }
}
