package gr36.clubActiv.exeption_handling.exeptions;

public class RoleNotFoundException extends RuntimeException{

  public RoleNotFoundException(String roleName) {
    super(String.format("Role %s not found in the database", roleName));
  }

}
