package gr36.clubActiv.security.security_service;

import gr36.clubActiv.domain.entity.Role;
import gr36.clubActiv.repository.RoleRepository;
import gr36.clubActiv.security.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private final SecretKey accessKey;
  private final SecretKey refreshKey;
  private final RoleRepository roleRepository;

  // Constructor with secret keys injected from application properties
  public TokenService(
      @Value("${key.access}") String accessSecretPhrase,
      @Value("${key.refresh}") String refreshSecretPhrase,
      RoleRepository roleRepository
  ) {
    this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretPhrase));
    this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretPhrase));
    this.roleRepository = roleRepository;
  }

  // Generate access token
  public String generateAccessToken(UserDetails user) {
    LocalDateTime currentDate = LocalDateTime.now();
    Instant expirationInstant = currentDate.plusDays(7).atZone(ZoneId.systemDefault()).toInstant(); // Expires in 7 days
    Date expiration = Date.from(expirationInstant);

    return Jwts.builder()
        .setSubject(user.getUsername())
        .setExpiration(expiration)
        .signWith(accessKey)
        .claim("roles", user.getAuthorities())  // Store roles
        .claim("name", user.getUsername())      // Store username
        .compact();
  }

  // Generate refresh token
  public String generateRefreshToken(UserDetails user) {
    LocalDateTime currentDate = LocalDateTime.now();
    Instant expirationInstant = currentDate.plusDays(30).atZone(ZoneId.systemDefault()).toInstant(); // Expires in 30 days
    Date expiration = Date.from(expirationInstant);

    return Jwts.builder()
        .setSubject(user.getUsername())
        .setExpiration(expiration)
        .signWith(refreshKey)
        .compact();
  }

  // Validate access token
  public boolean validateAccessToken(String accessToken) {
    return validateToken(accessToken, accessKey);
  }

  // Validate refresh token
  public boolean validateRefreshToken(String refreshToken) {
    return validateToken(refreshToken, refreshKey);
  }

  private boolean validateToken(String token, SecretKey key) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false; // Token is invalid
    }
  }

  // Get access token claims
  public Claims getAccessClaims(String accessToken) {
    return getClaims(accessToken, accessKey);
  }

  // Get refresh token claims
  public Claims getRefreshClaims(String refreshToken) {
    return getClaims(refreshToken, refreshKey);
  }

  private Claims getClaims(String token, SecretKey key) {
    return Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // Map claims to AuthInfo object
  public AuthInfo mapClaimsToAuthInfo(Claims claims) {
    String username = claims.getSubject();

    // Extract roles from claims
    List<LinkedHashMap<String, String>> roleList = (List<LinkedHashMap<String, String>>) claims.get("roles");
    Set<Role> roles = new HashSet<>();

    // Map roles from the token to Role objects
    for (LinkedHashMap<String, String> roleEntry : roleList) {
      String roleTitle = roleEntry.get("authority");
      Role role = roleRepository.findByRole(roleTitle).orElseThrow(
          () -> new RuntimeException("Database doesn't contain role: " + roleTitle)
      );
      roles.add(role);
    }

    return new AuthInfo(username, roles);
  }
}
