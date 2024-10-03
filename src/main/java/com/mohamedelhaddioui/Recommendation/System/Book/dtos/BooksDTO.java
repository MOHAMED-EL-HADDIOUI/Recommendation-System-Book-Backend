package com.mohamedelhaddioui.Recommendation.System.Book.dtos;

import lombok.Data;

import java.util.List;

@Data
public class BooksDTO {
    List<BookDTO> bookDTOList;
    int totalpage ;
}