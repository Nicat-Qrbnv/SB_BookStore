package org.ltclab.sb_bookstore.dto.responseDTO.forAuthor;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;

@Data
public class AuthorResponseDTO {
    private String fullName;
    @JsonManagedReference
    private List<BookInAuthorResponseDTO> books;
}
