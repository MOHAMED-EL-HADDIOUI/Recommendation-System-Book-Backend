package com.mohamedelhaddioui.Recommendation.System.Book.dtos;

import lombok.Data;


@Data
public class UserDTO {
    private Long id; // unique identifier for each user
    private String location; // user location
    private Long age; // user age
    private String tel;
    private String gmail;
    private String nom;
    private String prenom;
    private String password;
}
