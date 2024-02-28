package org.ltclab.sb_bookstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookDTO {
    private String title;
    private List<String> categories;
    private int publicationYear;
    private double price;
    private String authorFullName;
}
