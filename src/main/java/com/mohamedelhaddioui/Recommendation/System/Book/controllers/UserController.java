package com.mohamedelhaddioui.Recommendation.System.Book.controllers;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.UsersDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import com.mohamedelhaddioui.Recommendation.System.Book.enums.Role;
import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.UserNotFoundException;
import com.mohamedelhaddioui.Recommendation.System.Book.security.auth.AuthenticationResponse;
import com.mohamedelhaddioui.Recommendation.System.Book.security.auth.AuthenticationService;
import com.mohamedelhaddioui.Recommendation.System.Book.security.auth.RegisterRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.security.configurations.JwtService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private UserService userService;
    private JwtService jwtService;
    private AuthenticationService service;

    public UserController(UserService userService,JwtService jwtService,AuthenticationService authenticationService)
    {
        this.userService = userService;
        this.jwtService = jwtService;
        this.service = authenticationService;
    }
    @GetMapping("/size")
    public int getSizeUsers(){
        int size = userService.getListUsers().size();
        return size;
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long userId) {
        System.out.println("delete book");
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
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

    @PutMapping("/update/{id_user}")
    public ResponseEntity<user> updateUser(@PathVariable Long id_user, @RequestBody RegisterRequest request_) {
        System.out.println("update user "+id_user);
        user user = userService.getUser(id_user);
        user.setNom(request_.getNom());
        user.setPrenom(request_.getPrenom());
        user.setAge(request_.getAge());
        user.setLocation(request_.getLocation());
        user.setTel(request_.getTel());
        user user1 = userService.saveUser(user);
        return ResponseEntity.ok(user1);
    }

    @PostMapping("/adduser")
    public ResponseEntity<AuthenticationResponse> addUser(@RequestBody RegisterRequest request_) {
        System.out.println("add user ");
        RegisterRequest registerRequest = request_;
        registerRequest.setRole(Role.USER);
        return ResponseEntity.ok(service.register(registerRequest));
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
    @GetMapping("/search")
    public UsersDTO getUsers(@RequestParam(name = "keyword", defaultValue = "") String keyword, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "pagesize", defaultValue = "5") int pagesize, @RequestParam(name = "choix", defaultValue = "nom") String choix) throws UserNotFoundException {
        UsersDTO usersDTO = userService.getUsers("%" + keyword + "%",choix, page,pagesize);
        return usersDTO;
    }

}