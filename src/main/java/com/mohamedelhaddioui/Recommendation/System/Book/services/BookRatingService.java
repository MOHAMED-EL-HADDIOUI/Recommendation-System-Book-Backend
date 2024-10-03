package com.mohamedelhaddioui.Recommendation.System.Book.services;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.BookRating;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface BookRatingService {
    BookRating saveBookRating(BookRating bookRating);
    BookRating getBookRating(Long Id_BookRating);
    List<BookRating> getListBookRatings();
    void deleteBookRating (Long Id_BookRating);
}
