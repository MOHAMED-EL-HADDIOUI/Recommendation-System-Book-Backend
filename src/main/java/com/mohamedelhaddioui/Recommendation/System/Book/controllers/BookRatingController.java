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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RequestMapping("/api/v1/bookratings")
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
    @GetMapping("")
    public List<BookRating> getbookRatings(){
        List<BookRating> list = bookRatingService.getListBookRatings();
        return list;
    }
    @GetMapping("/{bookId}")
    public int getbookRating(HttpServletRequest request,@PathVariable("bookId")  String bookId){
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        int bookRating = bookRatingService.getRating(userId,bookId);
        return bookRating;
    }

    @PostMapping("/{bookId}/{rating}")
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
    @PostMapping("/save")
    public BookRating saveBook(@RequestBody BookRating bookRating)
    {
        BookRating bookRating1 = bookRatingService.saveBookRating(bookRating);
        return bookRating1;
    }
    @DeleteMapping("/{bookRatingId}")
    public ResponseEntity<Void> deletebookRating(@PathVariable("bookRatingId") Long id) {
        bookRatingService.deleteBookRating(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
