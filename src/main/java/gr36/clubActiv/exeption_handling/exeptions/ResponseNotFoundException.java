package gr36.clubActiv.exeption_handling.exeptions;


public class ResponseNotFoundException extends RuntimeException {

  public ResponseNotFoundException(String message) {
    super(message);
  }

  public ResponseNotFoundException(Long responseId) {
    super("Response with ID " + responseId + " not found.");
  }
}