package org.ltclab.sb_bookstore.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private String categoryName;

    @Override
    public String toString() {
        return " [" + categoryName + "] ";
    }
}
