package com.mohamedelhaddioui.Recommendation.System.Book.controllers;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BookDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BooksDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.BookRating;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.BookNotFoundException;
import com.mohamedelhaddioui.Recommendation.System.Book.mappers.BookMapperImplementation;
import com.mohamedelhaddioui.Recommendation.System.Book.security.auth.RegisterRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.security.configurations.JwtService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookRatingService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookService;
import com.mohamedelhaddioui.Recommendation.System.Book.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class BookRatingController {
    private BookRatingService bookRatingService;
    private BookService bookService;
    private UserService userService;
    private JwtService jwtService;
    @Autowired
    BookMapperImplementation dtoMapper;
    public BookRatingController(BookService bookService,BookRatingService bookRatingService,UserService userService,JwtService jwtService)
    {
        this.bookService = bookService;
        this.userService = userService;
        this.bookRatingService = bookRatingService;
        this.jwtService = jwtService;
    }
    @GetMapping("/bookRatings/{bookId}")
    public int getbookRating(HttpServletRequest request,@PathVariable("bookId")  String bookId){
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        int bookRating = bookRatingService.getRating(userId,bookId);
        return bookRating;
    }
    @PutMapping("/users/update")
    public ResponseEntity<user> updateUserProfile(HttpServletRequest request,@RequestBody RegisterRequest request_) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        user user = userService.getUser(userId);
        user.setPassword(request_.getPassword());
        user.setNom(request_.getNom());
        user.setPrenom(request_.getPrenom());
        user.setAge(request_.getAge());
        user.setLocation(request_.getLocation());
        user.setTel(request_.getTel());
        user user1 = userService.saveUser(user);
        return ResponseEntity.ok(user1);
    }
    @PostMapping("/bookRatings/{bookId}/{rating}")
    public ResponseEntity<BookRating>  postbookRating(HttpServletRequest request,@PathVariable("bookId")  String bookId,@PathVariable("rating")  String rating){
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        user user = userService.getUser(userId);
        book book = bookService.getBook(bookId);
        BookRating bookRating = new BookRating();
        bookRating.setBook(book);
        bookRating.setUser(user);
        bookRating.setRating(Integer.parseInt(rating));
        BookRating bookRating1 = bookRatingService.saveBookRating(bookRating);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookRating1);
    }

    @GetMapping("/bookRatings")
    public List<BookRating> getbookRatings(){
        List<BookRating> list = bookRatingService.getListBookRatings();
        return list;
    }
    @PostMapping("/bookRatings/save")
    public BookRating saveBook(@RequestBody BookRating bookRating)
    {
        BookRating bookRating1 = bookRatingService.saveBookRating(bookRating);
        return bookRating1;
    }
    @DeleteMapping("/bookRatings/{bookRatingId}")
    public ResponseEntity<Void> deletebookRating(@PathVariable("bookRatingId") Long id) {
        bookRatingService.deleteBookRating(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/bookRatings/books")
    public BooksDTO getBooksRatedByAuthenticatedUser(HttpServletRequest request,@RequestParam(name = "page", defaultValue = "0") int page) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        user user = userService.getUser(userId);
        List<book> books = bookRatingService.getBooksRatedByUser(user);
        Page<book> booksPages = convertListToPage(books,page,5) ;
        List<BookDTO> bookDTOList=booksPages.getContent().stream().map(c->dtoMapper.fromBook(c)).collect(Collectors.toList());
        BooksDTO booksDTO= new BooksDTO();
        booksDTO.setBookDTOList(bookDTOList);
        booksDTO.setTotalpage(booksPages.getTotalPages());
        return booksDTO;
    }
    public Page<book> convertListToPage(List<book> books, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());
        List<book> subList = books.subList(start, end);
        return new PageImpl<>(subList, pageable, books.size());
    }
    @GetMapping("/user")
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
