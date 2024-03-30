package org.ltclab.sb_bookstore.dto.responseDTO.forAuthor;

import lombok.Data;
import org.ltclab.sb_bookstore.dto.CategoryDTO;

import java.util.List;

@Data
public class BookInAuthorResponseDTO {
    private String title;
    private List<CategoryDTO> categories;
    private int publicationYear;
    private double price;
}
