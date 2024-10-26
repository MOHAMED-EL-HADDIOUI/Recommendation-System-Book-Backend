package com.mohamedelhaddioui.Recommendation.System.Book.dtos;

import lombok.Data;

import java.util.List;

@Data

public class UsersDTO {
    List<UserDTO> userDTOList;
    int totalpage ;
}
