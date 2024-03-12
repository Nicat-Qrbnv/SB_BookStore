package org.ltclab.sb_bookstore.dto.responseDTO;

import lombok.Data;
import org.ltclab.sb_bookstore.model.StoreElement;

import java.util.List;

@Data
public class BookResponseDTO implements StoreElement {
    private String title;
    private List<String> categories;
    private int publicationYear;
    private double price;
    private String authorFullName;
}
