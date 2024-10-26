package com.mohamedelhaddioui.Recommendation.System.Book.mappers;

import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BookDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.UserDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImplementation {
    public UserDTO fromUser(user user){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }
    public user fromUserDTO(UserDTO userDTO){
        user user = new user();
        BeanUtils.copyProperties(userDTO,user);
        return user;
    }
    public void updateUserFromDTO(UserDTO userDTO, user user) {
        user user1 = fromUserDTO(userDTO);
        BeanUtils.copyProperties(user, user1,"id");
    }
}
