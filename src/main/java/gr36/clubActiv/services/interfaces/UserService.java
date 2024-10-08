package gr36.clubActiv.services.interfaces;


import gr36.clubActiv.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  //UserDetailsService loadUserByUsername(String username) throws UsernameNotFoundException;
  void register(User user);

  void registrationConfirm(String code);

  List<User> findAll();

  Optional<User> findById(Long id);

  User save(User user);

  void delete(Long id);

  Optional<User> findByEmail(String email);
}
