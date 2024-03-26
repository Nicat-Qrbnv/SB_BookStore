package org.ltclab.sb_bookstore.dto.requestDTO;

import lombok.Data;
import org.ltclab.sb_bookstore.dto.CategoryDTO;

import java.util.List;

@Data
public class BookRequestDTO {
    private String title;
    private List<CategoryDTO> categoryDTOs;
    private int publicationYear;
    private double price;
    private AuthorRequestDTO authorRequestDTO;
}
