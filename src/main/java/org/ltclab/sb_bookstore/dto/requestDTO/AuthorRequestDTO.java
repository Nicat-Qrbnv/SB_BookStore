package org.ltclab.sb_bookstore.dto.requestDTO;

import lombok.Data;

@Data
public class AuthorRequestDTO {
    private String fullName;

    @Override
    public String toString() {
        return "Author: "+ fullName;
    }
}
