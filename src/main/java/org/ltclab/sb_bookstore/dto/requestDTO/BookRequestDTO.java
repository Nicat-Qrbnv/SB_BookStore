package org.ltclab.sb_bookstore.dto.requestDTO;

import lombok.Data;

import java.util.List;

@Data
public class BookRequestDTO {
    private String title;
    private List<String> categories;
    private int publicationYear;
    private double price;
    private String authorFullName;
}
