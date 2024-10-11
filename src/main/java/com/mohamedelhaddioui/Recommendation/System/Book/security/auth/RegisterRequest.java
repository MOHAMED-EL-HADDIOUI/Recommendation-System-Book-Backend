package com.mohamedelhaddioui.Recommendation.System.Book.security.auth;

import com.mohamedelhaddioui.Recommendation.System.Book.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  private Long id; // unique identifier for each user
  private String prenom;
  private String nom;
  private String gmail;
  private String password;
  private Role role;
  private String location; // user location
  private Long age; // user age
  private String tel;
}
