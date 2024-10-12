package gr36.clubActiv.security.security_service;

import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.security.sec_dto.TokenResponseDto;
import gr36.clubActiv.services.UserServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  // fields
  private UserServiceImpl userService;
  private TokenService tokenService;
  private Map<String, String> refreshStorage;
  private BCryptPasswordEncoder encoder;
  private Map<String, String> refreshTokenStorage = new HashMap<>();

  // constructor

  public AuthService(UserServiceImpl userService, TokenService tokenService,  BCryptPasswordEncoder encoder) {
    this.userService = userService;
    this.tokenService = tokenService;
    this.refreshStorage = new HashMap<>();
    this.encoder = encoder;
  }

  // авторизация inboundUser - входящего пользователя
  public TokenResponseDto login(User inboundUser) throws AuthException {
    String username = inboundUser.getUsername();
    UserDetails foundUser = userService.loadUserByUsername(username);

    if (encoder.matches(inboundUser.getPassword(), foundUser.getPassword())) { // сравниваем пароль пользователя и пароль из БД от найденного пользователя
      String accessToken = tokenService.generateAccessToken(foundUser);
      String refreshToken = tokenService.generateRefreshToken(foundUser);
      refreshStorage.put(username, refreshToken);
      return new TokenResponseDto(accessToken, refreshToken); // вернули пользователю токены
    }
    throw new AuthException("Password is incorrect");
  }

  // проверка и выдача нового refreshToken
  public TokenResponseDto getNewAccessToken(String inboundRefreshToken) throws AuthException {
    Claims refreshClaims = tokenService.getRefreshClaims(inboundRefreshToken);
    String username = refreshClaims.getSubject();
    String foundRefreshToken = refreshStorage.get(username);

    if (foundRefreshToken != null && foundRefreshToken.equals(inboundRefreshToken)) {
      UserDetails foundUser = userService.loadUserByUsername(username);
      String accessToken = tokenService.generateAccessToken(foundUser);
      return new TokenResponseDto(accessToken, null);
    }
    throw new AuthException("Refresh token is incorrect");
  }


  public void logout(String refreshToken) {
    refreshTokenStorage.remove(refreshToken);
  }
}
