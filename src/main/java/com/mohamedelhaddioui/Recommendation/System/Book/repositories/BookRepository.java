package com.mohamedelhaddioui.Recommendation.System.Book.repositories;

import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface BookRepository extends JpaRepository<book,String> {
    @Query("select b from book b where b.bookTitle like :kw")
    Page<book> searchByBookTitle(@Param("kw") String keyword, Pageable pageable);
    Page<book> findAll(Pageable pageable);
}
