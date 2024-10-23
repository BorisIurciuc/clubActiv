package gr36.clubActiv;

import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.services.interfaces.ActivityService;
import gr36.clubActiv.services.interfaces.RoleService;
import gr36.clubActiv.services.interfaces.UserService;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  private final UserService userService;
  private final RoleService roleService;
  private final ActivityService activityService;
  private final BCryptPasswordEncoder encoder;

  public DataInitializer(UserService userService, RoleService roleService,
      ActivityService activityService, BCryptPasswordEncoder encoder) {
    this.userService = userService;
    this.roleService = roleService;
    this.activityService = activityService;
    this.encoder = encoder;
  }

  @Override
  public void run(String... args) throws Exception {
    if (userService.countAdmins() == 0) {
      User admin = new User();
      admin.setUsername("TEST_ADMIN");
      admin.setEmail("testadmin@example.com");
      admin.setPassword(encoder.encode("111"));
      admin.setRoles(Set.of(roleService.getRoleAdmin()));
      admin.setActive(true);

      userService.save(admin);
      System.out.println("Admin user created: TEST_ADMIN");
    } else {
      System.out.println("Admin user already exists. No new admin created.");
    }


    if (!userService.existsByUsername("TEST_USER")) {
      User user = new User();
      user.setUsername("TEST_USER");
      user.setEmail("testuser@example.com");
      user.setPassword(encoder.encode("111"));
      user.setRoles(Set.of(roleService.getRoleUser()));
      user.setActive(true);

      userService.save(user);
      System.out.println("Regular user created: TEST_USER");
    }
  }
}