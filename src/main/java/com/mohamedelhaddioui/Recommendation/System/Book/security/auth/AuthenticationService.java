package com.mohamedelhaddioui.Recommendation.System.Book.security.auth;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.Token;
import com.mohamedelhaddioui.Recommendation.System.Book.enums.TokenType;
import com.mohamedelhaddioui.Recommendation.System.Book.repositories.TokenRepository;
import com.mohamedelhaddioui.Recommendation.System.Book.repositories.UserRepository;
import com.mohamedelhaddioui.Recommendation.System.Book.security.configurations.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    // Build the user object
    var user = com.mohamedelhaddioui.Recommendation.System.Book.entites.user.builder()
            .prenom(request.getPrenom())
            .nom(request.getNom())
            .id(request.getId())
            .tel(request.getTel())
            .age(request.getAge())
            .location(request.getLocation())
            .gmail(request.getGmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();

    // Save the user to the repository
    var savedUser = repository.save(user);

    // Generate tokens with the userId
    var jwtToken = jwtService.generateToken(savedUser, savedUser.getId()); // Include userId
    var refreshToken = jwtService.generateRefreshToken(savedUser, savedUser.getId()); // Include userId

    // Save the user's token in the repository
    saveUserToken(savedUser, jwtToken);

    // Return the response with access and refresh tokens
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    // Authenticate the user
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getGmail(),
                    request.getPassword()
            )
    );

    // Retrieve the user from the repository
    var user = repository.findByGmail(request.getGmail())
            .orElseThrow();

    // Generate tokens with the userId
    var jwtToken = jwtService.generateToken(user, user.getId()); // Include userId
    var refreshToken = jwtService.generateRefreshToken(user, user.getId()); // Include userId

    // Revoke any existing tokens for the user
    revokeAllUserTokens(user);

    // Save the new token
    saveUserToken(user, jwtToken);

    // Return the response with access and refresh tokens
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  private void saveUserToken(user user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(user user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }

    // Extract the refresh token from the header
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);

    if (userEmail != null) {
      var user = this.repository.findByGmail(userEmail)
              .orElseThrow();

      // Verify if the token is valid
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user, user.getId()); // Include userId
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        // Return the new access and refresh tokens
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}

