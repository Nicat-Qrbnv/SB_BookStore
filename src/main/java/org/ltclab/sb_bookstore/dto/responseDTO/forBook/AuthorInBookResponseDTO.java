package org.ltclab.sb_bookstore.dto.responseDTO.forBook;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.ltclab.sb_bookstore.dto.responseDTO.forAuthor.BookInAuthorResponseDTO;

import java.util.List;

@Data
public class AuthorInBookResponseDTO {
    private String fullName;
}
