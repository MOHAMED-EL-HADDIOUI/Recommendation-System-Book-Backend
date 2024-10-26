package com.mohamedelhaddioui.Recommendation.System.Book.repositories;

import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<book,String> {
    @Query("select b from book b where b.bookTitle like :kw")
    Page<book> searchByBookTitle(@Param("kw") String keyword, Pageable pageable);
    @Query("select b from book b where b.publisher like :kw")
    Page<book> searchByPublisher(@Param("kw") String keyword, Pageable pageable);
    @Query("select b from book b where b.bookAuthor like :kw")
    Page<book> searchByBookAuthor(@Param("kw") String keyword, Pageable pageable);
    @Query("select b from book b where b.ISBN like :kw")
    Page<book> searchByISBN(@Param("kw") String keyword, Pageable pageable);
    Page<book> findAll(Pageable pageable);
    @Query("SELECT b FROM book b ORDER BY b.avgRating DESC LIMIT 5")
    List<book> findTop5ByAvgRating();
    // Requête pour obtenir les 5 livres avec le meilleur score calculé
    @Query("SELECT b FROM book b WHERE b.ratingCount >= 5 ORDER BY b.avgRating DESC LIMIT 5")
    List<book> findTop5ByWeightedRating();
}
