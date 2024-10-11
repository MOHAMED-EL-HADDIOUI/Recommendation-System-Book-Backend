package com.mohamedelhaddioui.Recommendation.System.Book.controllers;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.ChangePasswordRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
@RequestMapping("/api/v1")
@RestController
public class UserController {
    private UserService userService;
    public UserController(UserService userService)
    {
        this.userService = userService;
    }
    @GetMapping("/users/{userId}")
    public user getUser(@PathVariable("userId") Long userId){
        user user = userService.getUser(userId);
        return user;
    }
    @GetMapping("/users")
    public List<user> getUsers(){
        List<user> list = userService.getListUsers();
        return list;
    }
    @PostMapping("/users/save")
    public user saveUser(@RequestBody user user)
    {
        user user1 = userService.saveUser(user);
        return user1;
    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/")
    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

}
