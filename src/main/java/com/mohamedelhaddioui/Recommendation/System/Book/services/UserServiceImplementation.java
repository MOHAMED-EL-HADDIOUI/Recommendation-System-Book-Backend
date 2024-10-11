package com.mohamedelhaddioui.Recommendation.System.Book.services;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.ChangePasswordRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;

import java.security.Principal;
import java.util.List;

import com.mohamedelhaddioui.Recommendation.System.Book.repositories.BookRatingRepository;
import com.mohamedelhaddioui.Recommendation.System.Book.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Transactional
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService{
    @Autowired
    @Order(1)
    UserRepository userRepository;
    @Autowired
    @Order(1)
    PasswordEncoder passwordEncoder;
    @Override
    public user saveUser(user user) {
        return userRepository.save(user);
    }

    @Override
    public user getUser(Long Id_user) {
        user user = userRepository.getById(Id_user);
        if(user==null)
        {
            System.out.println("user n'est pas ");
            return null;
        }
        else {
            return user;
        }
    }

    @Override
    public List<user> getListUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long Id_user) {
        userRepository.deleteById(Id_user);
    }
    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (user) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }
}
