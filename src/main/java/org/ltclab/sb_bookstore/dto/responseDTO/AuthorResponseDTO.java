package org.ltclab.sb_bookstore.dto.responseDTO;

import java.util.List;

public class AuthorResponseDTO {
    private String fullName;
    private List<BookResponseDTO> books;

    @Override
    public String toString() {
        return "Author: " + fullName;
    }
}
