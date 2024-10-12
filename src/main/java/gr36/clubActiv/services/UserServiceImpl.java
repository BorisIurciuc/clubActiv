package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.Role;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.UserAlreadyExistsException;
import gr36.clubActiv.exeption_handling.exeptions.UserNotFoundException;
import gr36.clubActiv.repository.ConfirmationCodeRepository;
import gr36.clubActiv.repository.UserRepository;
import gr36.clubActiv.services.interfaces.ConfirmationService;
import gr36.clubActiv.services.interfaces.EmailService;
import gr36.clubActiv.services.interfaces.RoleService;
import gr36.clubActiv.services.interfaces.UserService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
  private final UserRepository repository;
  private final RoleService roleService;
  private final EmailService emailService;
  private final BCryptPasswordEncoder encoder;
  private final ConfirmationService confirmationService;
  private final ConfirmationCodeRepository confirmationCodeRepository;
  private final UserRepository userRepository;


  public UserServiceImpl(UserRepository repository, RoleService roleService,
      EmailService emailService, BCryptPasswordEncoder encoder,
      ConfirmationService confirmationService,
      ConfirmationCodeRepository confirmationCodeRepository, UserRepository userRepository) {
    this.repository = repository;
    this.roleService = roleService;
    this.emailService = emailService;
    this.encoder = encoder;
    this.confirmationService = confirmationService;
    this.confirmationCodeRepository = confirmationCodeRepository;
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException(String.format("User %s not found", username)));
  }

  @Override
  public void register(User user) {
    Optional<User> existingUser = repository.findByEmail(user.getEmail());
    if (existingUser.isPresent()) {
      throw new UserAlreadyExistsException("Email already registered");
    }
//test
    User newUser = new User();
    newUser.setUsername(user.getUsername());
    newUser.setRoles(Set.of(roleService.getRoleUser()));
    newUser.setEmail(user.getEmail());
    newUser.setPassword(encoder.encode(user.getPassword()));
    if (user.getImage() == null || user.getImage().isEmpty()) {
      user.setImage("https://upload.wikimedia.org/wikipedia/commons/2/2c/Default_pfp.svg");
    }
    newUser.setImage(user.getImage());
    newUser.setActive(false);

    repository.save(newUser);
    emailService.sendConfirmationEmail(newUser);
  }

  @Override
  public Optional<User> update(Long id, User updatedUser) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()
        || authentication.getPrincipal() == null) {
      throw new RuntimeException("Authenticated user not found");
    }

    String currentUsername = null;

    if (authentication.getPrincipal() instanceof UserDetails) {
      currentUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
    } else if (authentication.getPrincipal() instanceof String) {
      currentUsername = (String) authentication.getPrincipal();  // It's a username string
    }

    User authenticatedUser = userRepository.findByUsername(currentUsername)
        .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found"));

    boolean isAdmin = authenticatedUser.getRoles().stream()
        .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

    User userToUpdate = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

    if (!isAdmin && !authenticatedUser.getId().equals(userToUpdate.getId())) {
      throw new RuntimeException("You can only update your own profile");
    }

    if (updatedUser.getEmail() != null) {
      userToUpdate.setEmail(updatedUser.getEmail());
    }

    if (updatedUser.getUsername() != null) {
      userToUpdate.setUsername(updatedUser.getUsername());
    }

    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
      userToUpdate.setPassword(encoder.encode(updatedUser.getPassword()));
    }

    return Optional.of(userRepository.save(userToUpdate));
  }


  @Override
  public void registrationConfirm(String code) {
    User user = confirmationService.getUserByConfirmationCode(code);
    user.setActive(true);
    confirmationCodeRepository.deleteByUserId(user.getId());
  }

  @Override
  public List<User> findAll() {
    return repository.findAll();
  }

  @Override
  public Optional<User> findById(Long id) {
    return Optional.ofNullable(repository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id)));
  }

  @Override
  public User save(User user) {
    return repository.save(user);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new UserNotFoundException(id); // Это исключение будет перехвачено, если пользователь не найден.
    }

    confirmationCodeRepository.deleteByUserId(id);
    repository.deleteById(id);
  }

  @Override
  public boolean isLastAdmin(Long userId) {
    User user = repository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    boolean isAdmin = user.getRoles().contains(roleService.getRoleAdmin());

    if (isAdmin) {
      long adminCount = repository.countByRole("ROLE_ADMIN");
      return adminCount <= 1;

    }

    return false;
  }

  @Override
  public boolean existsByUsername(String username) {

    Optional<User> user = userRepository.findByUsername(username);

    return user.isPresent();
  }

  @Override
  public int countAdmins() {
    Role adminRole = roleService.getRoleAdmin();
    return userRepository.countByRolesContaining(adminRole);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }


}



