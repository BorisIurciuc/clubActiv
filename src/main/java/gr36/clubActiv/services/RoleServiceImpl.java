package gr36.clubActiv.services;


import gr36.clubActiv.domain.entity.Role;
import gr36.clubActiv.exeption_handling.exeptions.RoleNotFoundException;
import gr36.clubActiv.repository.RoleRepository;
import gr36.clubActiv.services.interfaces.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository repository;

  public RoleServiceImpl(RoleRepository repository) {
    this.repository = repository;
  }

  @Override
  public Role getRoleAdmin() {
    return repository.findByRole("ROLE_ADMIN").orElseThrow(
        () -> new RoleNotFoundException("ROLE_ADMIN")
    );
  }

  @Override
  public Role getRoleUser() {
    return repository.findByRole("ROLE_USER").orElseThrow(
        () -> new RoleNotFoundException("ROLE_USER")
    );
  }
}


