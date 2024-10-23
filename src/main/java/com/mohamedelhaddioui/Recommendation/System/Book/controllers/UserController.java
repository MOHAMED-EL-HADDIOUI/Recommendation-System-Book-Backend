package com.mohamedelhaddioui.Recommendation.System.Book.controllers;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.ChangePasswordRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import com.mohamedelhaddioui.Recommendation.System.Book.security.auth.RegisterRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.security.configurations.JwtService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private UserService userService;
    private JwtService jwtService;

    public UserController(UserService userService,JwtService jwtService)
    {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PutMapping("/update")
    public ResponseEntity<user> updateUserProfile(HttpServletRequest request, @RequestBody RegisterRequest request_) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        user user = userService.getUser(userId);
        user.setNom(request_.getNom());
        user.setPrenom(request_.getPrenom());
        user.setAge(request_.getAge());
        user.setLocation(request_.getLocation());
        user.setTel(request_.getTel());
        user user1 = userService.saveUser(user);
        return ResponseEntity.ok(user1);
    }
    @GetMapping("/get")
    public RegisterRequest getUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        user user = userService.getUser(userId);
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNom(user.getNom());
        registerRequest.setAge(user.getAge());
        registerRequest.setId(user.getId());
        registerRequest.setGmail(user.getGmail());
        registerRequest.setLocation(user.getLocation());
        registerRequest.setRole(user.getRole());
        registerRequest.setTel(user.getTel());
        registerRequest.setPrenom(user.getPrenom());
        return registerRequest;
    }



}