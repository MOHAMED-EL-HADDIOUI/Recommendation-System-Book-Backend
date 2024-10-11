package com.mohamedelhaddioui.Recommendation.System.Book.repositories;

import com.mohamedelhaddioui.Recommendation.System.Book.entites.BookRating;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRatingRepository extends JpaRepository<BookRating,Long> {
    List<BookRating> findByUser(user user);
    @Query("SELECT br FROM BookRating br WHERE br.user.id = :userId AND br.book.ISBN = :isbn")
    BookRating findRatingByUserIdAndBookIsbn(@Param("userId") Long userId, @Param("isbn") String isbn);
}
