package gr36.clubActiv.services;



import gr36.clubActiv.domain.entity.Activity;
import gr36.clubActiv.domain.entity.Role;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.UserAlreadyExistsException;
import gr36.clubActiv.exeption_handling.exeptions.UserNotFoundException;
import gr36.clubActiv.repository.ActivityRepository;
import gr36.clubActiv.repository.ConfirmationCodeRepository;
import gr36.clubActiv.repository.UserRepository;
import gr36.clubActiv.services.interfaces.ConfirmationService;
import gr36.clubActiv.services.interfaces.EmailService;
import gr36.clubActiv.services.interfaces.RoleService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private ActivityRepository activityRepository;

  @Mock
  private ConfirmationCodeRepository confirmationCodeRepository;

  @Mock
  private RoleService roleService;

  @Mock
  private EmailService emailService;

  @Mock
  private ConfirmationService confirmationService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  private User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    user = new User();
    user.setId(1L);
    user.setEmail("test@example.com");
    user.setUsername("testUser");
  }

  @Test
  void testRegisterUser() {
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
    doNothing().when(emailService).sendConfirmationEmail(any(User.class));

    userService.register(user);

    verify(userRepository, times(1)).save(user);
    verify(emailService, times(1)).sendConfirmationEmail(user);
  }

  @Test
  void testRegisterUserAlreadyExists() {
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

    assertThrows(UserAlreadyExistsException.class, () -> userService.register(user));
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void testUpdateUser() {
    User updatedUser = new User();
    updatedUser.setEmail("newEmail@example.com");

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    Optional<User> result = userService.update(1L, updatedUser);

    assertTrue(result.isPresent());
    assertEquals("newEmail@example.com", result.get().getEmail());
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void testDeleteUser() {
    when(userRepository.existsById(1L)).thenReturn(true);
    when(activityRepository.findByAuthorId(1L)).thenReturn(List.of(new Activity()));

    userService.delete(1L);

    verify(activityRepository, times(1)).deleteAll(anyList());
    verify(confirmationCodeRepository, times(1)).deleteByUserId(1L);
    verify(userRepository, times(1)).deleteById(1L);
  }

  @Test
  void testFindById_UserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.findById(1L).orElseThrow(() -> new UserNotFoundException(1L)));
  }

  @Test
  void testFindByUsername() {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

    Optional<User> result = userService.findByUsername("testUser");

    assertTrue(result.isPresent());
    assertEquals("testUser", result.get().getUsername());
  }
}
