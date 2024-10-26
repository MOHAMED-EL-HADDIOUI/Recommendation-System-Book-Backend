package com.mohamedelhaddioui.Recommendation.System.Book.services;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BookDTO;
import java.util.List;

import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BooksDTO;
import org.springframework.data.domain.PageRequest;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.BookNotFoundException;
import com.mohamedelhaddioui.Recommendation.System.Book.mappers.BookMapperImplementation;
import com.mohamedelhaddioui.Recommendation.System.Book.repositories.BookRepository;
import jakarta.transaction.Transactional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Transactional
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class BookServiceImplementation implements BookService{
    @Autowired
    @Order(1)
    BookRepository bookRepository;
    @Autowired
    BookMapperImplementation dtoMapper;
    @Override
    public book saveBook(book book) {
        return bookRepository.save(book);
    }

    @Override
    public book getBook(String Id_book) {
        book book= bookRepository.getById(Id_book);
        if(book==null)
        {
            System.out.println("book n'est pas ");
            return null;
        }
        else {
            return book;
        }
    }

    @Override
    public List<book> getListBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void deletebook(String Id_book) {
        bookRepository.deleteById(Id_book);
    }

    @Override
    public book saveBookDTO(BookDTO bookDTO) {
        book book = dtoMapper.fromBookDTO(bookDTO);
        return bookRepository.save(book);
    }

    @Override
    public BooksDTO getBookByNom(String keyword, int page) throws BookNotFoundException {
        Page<book> books ;
        books = bookRepository.searchByBookTitle(keyword,PageRequest.of(page,60));
        List<BookDTO> bookDTOList=books.getContent().stream().map(c->dtoMapper.fromBook(c)).collect(Collectors.toList());
        if (books == null)
            throw new BookNotFoundException("book not fount");

        BooksDTO booksDTO= new BooksDTO();
        booksDTO.setBookDTOList(bookDTOList);
        booksDTO.setTotalpage(books.getTotalPages());
        return booksDTO;
    }

    @Override
    public BooksDTO getBooks(String kw, String choix, int page, int pageSize) throws BookNotFoundException {
        Page<book> books ;
        BooksDTO booksDTO= new BooksDTO();

        // Sélection de la méthode de recherche en fonction de `choix`
        if ("title".equals(choix)) {
            books = bookRepository.searchByBookTitle(kw, PageRequest.of(page, pageSize));
        } else if ("isbn".equals(choix)) {
            books = bookRepository.searchByISBN(kw, PageRequest.of(page, pageSize));
        } else if ("editor".equals(choix)) {
            books = bookRepository.searchByPublisher(kw, PageRequest.of(page, pageSize));
        } else if ("autor".equals(choix)) {
            books = bookRepository.searchByBookAuthor(kw, PageRequest.of(page, pageSize));
        } else {
            throw new IllegalArgumentException("Choix de recherche non valide");
        }

        // Vérification de l'existence de résultats
        if (books == null || books.isEmpty()) {
            throw new BookNotFoundException("Book not found");
        }

        List<BookDTO> bookDTOList = books.getContent()
                .stream()
                .map(dtoMapper::fromBook)
                .collect(Collectors.toList());

        booksDTO.setBookDTOList(bookDTOList);
        booksDTO.setTotalpage(books.getTotalPages());

        return booksDTO;
    }


    @Override
    public List<BookDTO> listBooksByPage(int page) {
        Page<book> books = bookRepository.findAll(PageRequest.of(page,60));
        List<BookDTO> collect = books.stream().map(customer -> dtoMapper.fromBook(customer)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public book updateBook(String Id_book, BookDTO bookDTO) {
        book existingbook = bookRepository.getById(Id_book);
        dtoMapper.updateBookFromDTO(bookDTO, existingbook);
        // Save the updated patient
        return bookRepository.save(existingbook);
    }
    @Override
    public List<book> getTop5RatedBooks() {
        return bookRepository.findTop5ByAvgRating();
    }
    @Override
    public List<book> findTop5ByWeightedRating() {
        return bookRepository.findTop5ByWeightedRating();
    }
}
