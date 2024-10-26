package com.mohamedelhaddioui.Recommendation.System.Book.services;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BookDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BooksDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.UserDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.UsersDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.ChangePasswordRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.BookNotFoundException;
import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.UserNotFoundException;
import com.mohamedelhaddioui.Recommendation.System.Book.mappers.BookMapperImplementation;
import com.mohamedelhaddioui.Recommendation.System.Book.mappers.UserMapperImplementation;
import com.mohamedelhaddioui.Recommendation.System.Book.repositories.BookRatingRepository;
import com.mohamedelhaddioui.Recommendation.System.Book.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.parameters.P;
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
    @Autowired
    UserMapperImplementation dtoMapper;
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

    @Override
    public UsersDTO getUsers(String kw, String choix, int page,int pageSize) throws UserNotFoundException {
        Page<user> users;
        UsersDTO usersDTO = new UsersDTO();

        // Sélection de la méthode de recherche en fonction de `choix`
        if ("nom".equals(choix)) {
            users = userRepository.searchByUserNom(kw, PageRequest.of(page, pageSize));
        } else if ("gmail".equals(choix)) {
            users = userRepository.searchByUserGmail(kw, PageRequest.of(page, pageSize));
        } else if ("location".equals(choix)) {
            users = userRepository.searchByUserLocation(kw, PageRequest.of(page, pageSize));
        } else if ("prenom".equals(choix)) {
            users = userRepository.searchByUserPrenom(kw, PageRequest.of(page, pageSize));
        } else {
            throw new IllegalArgumentException("Choix de recherche non valide");
        }

        // Vérification de l'existence de résultats
        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        // Conversion des entités User en UserDTO
        List<UserDTO> userDTOList = users.getContent()
                .stream()
                .map(dtoMapper::fromUser)
                .collect(Collectors.toList());

        // Attribution des résultats dans UsersDTO
        usersDTO.setUserDTOList(userDTOList);
        usersDTO.setTotalpage(users.getTotalPages());

        return usersDTO;
    }
}
