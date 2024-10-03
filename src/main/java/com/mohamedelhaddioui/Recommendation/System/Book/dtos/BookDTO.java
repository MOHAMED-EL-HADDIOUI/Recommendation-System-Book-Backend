package com.mohamedelhaddioui.Recommendation.System.Book.dtos;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class BookDTO {
    private String ISBN; // unique identifier for each book
    private String bookTitle; // title of the book
    private String bookAuthor; // author of the book
    private Long yearOfPublication; // publication year
    private String publisher; // publisher of the book
    private String imageURLS; // small image URL
    private String imageURLM; // medium image URL
    private String imageURLL; // large image URL
}
