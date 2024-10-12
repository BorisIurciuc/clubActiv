package gr36.clubActiv.repository;

import gr36.clubActiv.domain.entity.Role;
import gr36.clubActiv.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // Return Optional
    Optional<User> findByUsername(String username);
    // Add a method to count admins
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.role = 'ROLE_ADMIN'")
    long countByRole(String role);

    int countByRolesContaining(Role role);
}
