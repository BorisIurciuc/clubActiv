package gr36.clubActiv.controller;


import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.UserNotFoundException;
import gr36.clubActiv.services.interfaces.UserService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final BCryptPasswordEncoder encoder;

  public UserController(UserService userService, BCryptPasswordEncoder encoder) {
    this.userService = userService;
    this.encoder = encoder;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.findAll();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return userService.findById(id)
        .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
    return userService.update(id, updatedUser)
        .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }


  @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    Optional<User> optionalUser = userService.findById(id);

    if (optionalUser.isPresent()) {
      User user = optionalUser.get();

      if (userService.isLastAdmin(id)) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden, если это последний админ
      }

      userService.delete(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content, если удаление прошло успешно
    } else {
      throw new UserNotFoundException(id); // Выбрасываем исключение, если пользователь не найден
    }
  }





}







