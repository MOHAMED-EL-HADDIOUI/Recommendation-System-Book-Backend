package com.mohamedelhaddioui.Recommendation.System.Book.services;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BookDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BooksDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.BookRating;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.BookNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface BookService {
    book saveBook(book book);
    book getBook(String Id_book);
    List<book> getListBooks();
    void deletebook (String Id_book);
    book saveBookDTO(BookDTO bookDTO);
    BooksDTO getBookByNom(String s, int page) throws BookNotFoundException;
    BooksDTO getBooks(String kw, String choix, int page,int pageSize) throws BookNotFoundException;

    List<BookDTO> listBooksByPage(int page);
    book updateBook(String Id_book, BookDTO bookDTO);
    public List<book> getTop5RatedBooks();

    public List<book> findTop5ByWeightedRating();
}
