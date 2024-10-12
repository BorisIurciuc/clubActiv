package gr36.clubActiv.services.interfaces;


import gr36.clubActiv.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {


  void register(User user);

  Optional<User> update( Long id, User updatedUser);



  void registrationConfirm(String code);

  List<User> findAll();

  Optional<User> findById(Long id);

  User save(User user);

  void delete(Long id);


  boolean isLastAdmin(Long userId);

  boolean existsByUsername(String testUser);

  int countAdmins();

  Optional<User> findByUsername(String username);
}
