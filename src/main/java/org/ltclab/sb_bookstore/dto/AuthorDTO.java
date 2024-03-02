package org.ltclab.sb_bookstore.dto;

import lombok.Data;

@Data
public class AuthorDTO {
    private String fullName;

    @Override
    public String toString() {
        return "Author: "+ fullName;
    }
}
