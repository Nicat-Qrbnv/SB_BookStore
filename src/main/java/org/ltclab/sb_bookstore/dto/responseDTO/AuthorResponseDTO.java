package org.ltclab.sb_bookstore.dto.responseDTO;

import org.ltclab.sb_bookstore.util.ListToString;

import java.util.List;

public class AuthorResponseDTO {
    private String fullName;
    private List<BookResponseDTO> books;

    @Override
    public String toString() {
        return "Author: " + fullName + "\nBooks:\n" + ListToString.convert(books);
    }
}
