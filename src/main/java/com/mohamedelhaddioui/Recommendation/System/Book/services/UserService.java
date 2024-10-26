package com.mohamedelhaddioui.Recommendation.System.Book.services;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BooksDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.UsersDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.ChangePasswordRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.BookNotFoundException;
import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface UserService {
    user saveUser(user user);
    user getUser(Long Id_user);
    List<user> getListUsers();
    void deleteUser (Long Id_user);
    public void changePassword(ChangePasswordRequest request, Principal connectedUser);
    UsersDTO getUsers(String kw, String choix, int page,int pageSize) throws UserNotFoundException;

}
