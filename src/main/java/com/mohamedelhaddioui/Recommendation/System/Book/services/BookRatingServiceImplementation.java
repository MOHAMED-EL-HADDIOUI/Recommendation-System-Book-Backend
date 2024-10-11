package com.mohamedelhaddioui.Recommendation.System.Book.services;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.BookRating;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import com.mohamedelhaddioui.Recommendation.System.Book.repositories.BookRatingRepository;
import com.mohamedelhaddioui.Recommendation.System.Book.repositories.BookRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class BookRatingServiceImplementation implements BookRatingService{
    @Autowired
    @Order(1)
    BookRatingRepository bookRatingRepository;
    @Override
    public BookRating saveBookRating(BookRating bookRating) {
        return bookRatingRepository.save(bookRating);
    }
    @Override
    public BookRating getBookRating(Long Id_BookRating) {
        return bookRatingRepository.getById(Id_BookRating);
    }

    @Override
    public List<BookRating> getListBookRatings() {
        return bookRatingRepository.findAll();
    }

    @Override
    public void deleteBookRating(Long Id_BookRating) {
        bookRatingRepository.deleteById(Id_BookRating);
    }

    @Override
    public List<book> getBooksRatedByUser(user user) {
        List<BookRating> ratings = bookRatingRepository.findByUser(user);

        // Extract the books from the ratings
        return ratings.stream()
                .map(BookRating::getBook)  // Get the book from each rating
                .collect(Collectors.toList());
    }
    @Override
    public int getRating(Long userId, String isbn) {
        BookRating bookRating = bookRatingRepository.findRatingByUserIdAndBookIsbn(userId, isbn);
        return bookRating.getRating(); // Get the rating if present
    }

}
