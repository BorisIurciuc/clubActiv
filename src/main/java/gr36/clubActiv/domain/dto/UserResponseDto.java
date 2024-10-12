package gr36.clubActiv.domain.dto;

import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.domain.entity.Role;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserResponseDto {

  private Long id;
  private String username;
  private String email;
  private List<String> roles;
  private boolean active;

  public UserResponseDto(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.roles = user.getRoles().stream()
        .map(Role::getRole)
        .collect(Collectors.toList());
    this.active = user.isActive();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserResponseDto that)) {
      return false;
    }
    return active == that.active && Objects.equals(id, that.id) && Objects.equals(
        username, that.username) && Objects.equals(email, that.email)
        && Objects.equals(roles, that.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, email, roles, active);
  }

  @Override
  public String toString() {
    return "UserResponseDto{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", roles=" + roles +
        ", active=" + active +
        '}';
  }
}
