package com.mohamedelhaddioui.Recommendation.System.Book.entites;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "isbn")
@Table(name = "books")
public class book {
    @Id
    private String ISBN; // unique identifier for each book
    private String bookTitle; // title of the book
    private String bookAuthor; // author of the book

    private Long yearOfPublication; // publication year
    private String publisher; // publisher of the book

    private String imageURLS; // small image URL

    private String imageURLM; // medium image URL

    private String imageURLL; // large image URL
    private Double avgRating;
    private Long ratingCount;
}
