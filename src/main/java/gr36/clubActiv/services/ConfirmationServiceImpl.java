package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.ConfirmationCode;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.ConfirmationFailedException;
import gr36.clubActiv.repository.ConfirmationCodeRepository;
import gr36.clubActiv.services.interfaces.ConfirmationService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

  private final ConfirmationCodeRepository repository;

  public ConfirmationServiceImpl(ConfirmationCodeRepository repository) {
    this.repository = repository;
  }

  @Override
  public String generateConfirmationCode(User user) {


    LocalDateTime expired = LocalDateTime.now().plusMinutes(60);


    String code = UUID.randomUUID().toString();


    ConfirmationCode entity = new ConfirmationCode(code, expired, user);


    repository.save(entity);

    return code;
  }



  @Override
  public User getUserByConfirmationCode(String code) {
    ConfirmationCode entity = repository.findByCode(code).orElse(null);

    if (entity == null) {
      throw new ConfirmationFailedException("Confirmation code not found");
    }

    if (LocalDateTime.now().isAfter(entity.getExpired())) {
      throw new ConfirmationFailedException("Confirmation code expired");
    }

    return entity.getUser();

  }



  @Override
  public void deleteByUserId(Long userId) {
    repository.deleteByUserId(userId);
  }


}
