package com.mohamedelhaddioui.Recommendation.System.Book.services;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.ChangePasswordRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
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
}
