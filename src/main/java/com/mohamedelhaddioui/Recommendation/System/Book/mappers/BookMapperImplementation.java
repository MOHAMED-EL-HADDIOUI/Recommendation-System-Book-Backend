package com.mohamedelhaddioui.Recommendation.System.Book.mappers;

import com.mohamedelhaddioui.Recommendation.System.Book.dtos.BookDTO;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
@Service
public class BookMapperImplementation {
    public BookDTO fromBook(book book){
        BookDTO bookDTO = new BookDTO();
        BeanUtils.copyProperties(book,bookDTO);
        return bookDTO;
    }
    public book fromBookDTO(BookDTO bookDTO){
        book book = new book();
        BeanUtils.copyProperties(bookDTO,book);
        return book;
    }
    public void updateBookFromDTO(BookDTO bookDTO, book existingPatient) {
        book updatedPatient = fromBookDTO(bookDTO);
        BeanUtils.copyProperties(updatedPatient, existingPatient,"id");
    }
}
