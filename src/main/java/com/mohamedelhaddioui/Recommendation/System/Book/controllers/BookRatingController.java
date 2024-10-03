package com.mohamedelhaddioui.Recommendation.System.Book.controllers;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.BookRating;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookRatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
public class BookRatingController {
    private BookRatingService bookRatingService;
    public BookRatingController(BookRatingService bookRatingService)
    {
        this.bookRatingService = bookRatingService;
    }
    @GetMapping("/bookRatings/{bookRatingId}")
    public BookRating getbookRating(@PathVariable("bookRatingId")  Long bookRatingId){
        BookRating bookRating = bookRatingService.getBookRating(bookRatingId);
        return bookRating;
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
}
