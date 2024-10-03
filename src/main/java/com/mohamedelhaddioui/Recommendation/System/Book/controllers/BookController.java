package com.mohamedelhaddioui.Recommendation.System.Book.controllers;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BookDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BooksDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.exceptions.BookNotFoundException;
import com.mohamedelhaddioui.Recommendation.System.Book.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
public class BookController {
    private BookService bookService;
    public BookController(BookService bookService)
    {
        this.bookService = bookService;
    }
    @GetMapping("/books/{bookId}")
    public book getBook(@PathVariable String bookId){
        System.out.println("get book "+bookId);
        book book = bookService.getBook(bookId);
        return book;
    }
    @GetMapping("/books")
    public List<book> getbooks(){
        List<book> list = bookService.getListBooks();
        return list;
    }
    @PostMapping("/books/save")
    public book saveBook(@RequestBody book book)
    {
        System.out.println("save book "+book.getISBN());
        book book1 = bookService.saveBook(book);
        return book1;
    }
    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {
        bookService.deletebook(bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/books/savebook/")
    public ResponseEntity<String> savebook(@RequestBody BookDTO bookDTO)
    {
        book book = bookService.saveBookDTO(bookDTO);
        return ResponseEntity.ok(book.getISBN());
    }
    @GetMapping("/books/search")
    public BooksDTO getPatientByName(@RequestParam(name = "keyword", defaultValue = "") String keyword, @RequestParam(name = "page", defaultValue = "0") int page) throws BookNotFoundException {
        BooksDTO patientsDTO = bookService.getBookByNom("%" + keyword + "%", page);
        return patientsDTO;
    }
    @GetMapping("/books/page/{page}")
    public List<BookDTO> patients(@PathVariable int page) {
        return bookService.listBooksByPage(page);
    }
    @PutMapping("/books/updatebook/{bookId}")
    public book updateBook(@PathVariable String bookId, @RequestBody BookDTO bookDTO) {
        System.out.println("update book "+bookId);
        return bookService.updateBook(bookId, bookDTO);
    }
}
