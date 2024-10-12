package gr36.clubActiv.security.sec_filter;

import gr36.clubActiv.security.AuthInfo;
import gr36.clubActiv.security.security_service.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class TokenFilter extends GenericFilterBean {

  private final TokenService tokenService;

  public TokenFilter(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    // Extract JWT token from the request
    String token = getTokenFromRequest((HttpServletRequest) servletRequest);

    if (token != null && tokenService.validateAccessToken(token)) {
      // If the token is valid, extract claims and set authentication in the security context
      Claims claims = tokenService.getAccessClaims(token);
      AuthInfo authInfo = tokenService.mapClaimsToAuthInfo(claims);
      authInfo.setAuthenticated(true);
      SecurityContextHolder.getContext().setAuthentication(authInfo);
    }


    filterChain.doFilter(servletRequest, servletResponse);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    // Extract the "Authorization" header and remove the "Bearer " prefix if it exists
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7); // Remove "Bearer " prefix
    }
    return null;
  }
}
