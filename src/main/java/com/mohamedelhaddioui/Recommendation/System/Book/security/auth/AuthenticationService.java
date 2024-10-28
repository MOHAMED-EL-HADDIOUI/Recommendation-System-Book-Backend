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
    // Vérification des champs vides
    if (request.getPrenom() == null || request.getPrenom().isEmpty()) {
      throw new IllegalArgumentException("Le prénom est requis.");
    }
    if (request.getNom() == null || request.getNom().isEmpty()) {
      throw new IllegalArgumentException("Le nom est requis.");
    }
    if (request.getGmail() == null || request.getGmail().isEmpty() || !request.getGmail().contains("@")) {
      throw new IllegalArgumentException("L'adresse e-mail est invalide.");
    }
    if (request.getPassword() == null || request.getPassword().isEmpty() || request.getPassword().length() < 8) {
      throw new IllegalArgumentException("Le mot de passe doit comporter au moins 8 caractères.");
    }
    if (request.getAge() == null || request.getAge() <= 0) {
      throw new IllegalArgumentException("L'âge doit être un nombre valide.");
    }
    if (request.getTel() == null || request.getTel().isEmpty()) {
      throw new IllegalArgumentException("Le numéro de téléphone est requis.");
    }
    if (request.getLocation() == null || request.getLocation().isEmpty()) {
      throw new IllegalArgumentException("Le lieu est requis.");
    }

    // Vérification si l'utilisateur existe déjà
    if (repository.findByGmail(request.getGmail()).isPresent()) {
      throw new IllegalArgumentException("Un utilisateur avec cette adresse e-mail existe déjà.");
    }

    // Création de l'objet utilisateur
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

    // Enregistrement de l'utilisateur dans le repository
    var savedUser = repository.save(user);

    // Génération des tokens avec l'ID de l'utilisateur
    var jwtToken = jwtService.generateToken(savedUser, savedUser.getId(),savedUser.getRole().name());
    var refreshToken = jwtService.generateRefreshToken(savedUser, savedUser.getId(),savedUser.getRole().name());

    // Sauvegarde du token utilisateur
    saveUserToken(savedUser, jwtToken);

    // Retourne la réponse avec les tokens d'accès et de rafraîchissement
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
    var jwtToken = jwtService.generateToken(user, user.getId(),user.getRole().name()); // Include userId
    var refreshToken = jwtService.generateRefreshToken(user, user.getId(),user.getRole().name()); // Include userId

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
        var accessToken = jwtService.generateToken(user, user.getId(),user.getRole().name()); // Include userId
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

