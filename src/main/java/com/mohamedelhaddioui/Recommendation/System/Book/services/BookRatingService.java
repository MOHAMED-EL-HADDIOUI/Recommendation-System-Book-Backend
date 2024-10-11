package com.mohamedelhaddioui.Recommendation.System.Book.services;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.BookRating;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface BookRatingService {
    BookRating saveBookRating(BookRating bookRating);
    BookRating getBookRating(Long Id_BookRating);
    List<BookRating> getListBookRatings();
    void deleteBookRating (Long Id_BookRating);
    public List<book> getBooksRatedByUser(user user);
    public int getRating(Long userId, String isbn);
    }
