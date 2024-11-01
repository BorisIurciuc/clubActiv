package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.ConfirmationCode;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.ConfirmationFailedException;
import gr36.clubActiv.repository.ConfirmationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfirmationServiceImplTest {

  @Mock
  private ConfirmationCodeRepository confirmationCodeRepository;

  @InjectMocks
  private ConfirmationServiceImpl confirmationService;

  private User user;
  private ConfirmationCode confirmationCode;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    user = new User();
    user.setId(1L);
    user.setEmail("test@example.com");

    confirmationCode = new ConfirmationCode();
    confirmationCode.setCode(UUID.randomUUID().toString());
    confirmationCode.setExpired(LocalDateTime.now().plusMinutes(60));
    confirmationCode.setUser(user);
  }

  @Test
  void testGenerateConfirmationCode() {
    when(confirmationCodeRepository.save(any(ConfirmationCode.class))).thenReturn(confirmationCode);

    String generatedCode = confirmationService.generateConfirmationCode(user);

    assertNotNull(generatedCode);
    verify(confirmationCodeRepository, times(1)).save(any(ConfirmationCode.class));
  }

  @Test
  void testGetUserByConfirmationCode_Success() {
    when(confirmationCodeRepository.findByCode(confirmationCode.getCode())).thenReturn(Optional.of(confirmationCode));

    User result = confirmationService.getUserByConfirmationCode(confirmationCode.getCode());

    assertNotNull(result);
    assertEquals(user.getId(), result.getId());
    verify(confirmationCodeRepository, times(1)).findByCode(confirmationCode.getCode());
  }

  @Test
  void testGetUserByConfirmationCode_CodeNotFound() {
    when(confirmationCodeRepository.findByCode("invalidCode")).thenReturn(Optional.empty());

    assertThrows(ConfirmationFailedException.class, () -> confirmationService.getUserByConfirmationCode("invalidCode"));
  }

  @Test
  void testGetUserByConfirmationCode_CodeExpired() {
    confirmationCode.setExpired(LocalDateTime.now().minusMinutes(1));
    when(confirmationCodeRepository.findByCode(confirmationCode.getCode())).thenReturn(Optional.of(confirmationCode));

    assertThrows(ConfirmationFailedException.class, () -> confirmationService.getUserByConfirmationCode(confirmationCode.getCode()));
  }

  @Test
  void testDeleteByUserId() {
    doNothing().when(confirmationCodeRepository).deleteByUserId(user.getId());

    confirmationService.deleteByUserId(user.getId());

    verify(confirmationCodeRepository, times(1)).deleteByUserId(user.getId());
  }
}
