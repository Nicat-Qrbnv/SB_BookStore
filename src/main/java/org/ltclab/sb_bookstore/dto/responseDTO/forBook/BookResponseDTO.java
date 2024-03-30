package org.ltclab.sb_bookstore.dto.responseDTO.forBook;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.ltclab.sb_bookstore.dto.CategoryDTO;

import java.util.List;

@Data
public class BookResponseDTO {
    private String title;
    @JsonManagedReference
    private List<CategoryDTO> categories;
    private int publicationYear;
    private double price;
    @JsonManagedReference
    private AuthorInBookResponseDTO authorFullName;
}
