package org.ltclab.sb_bookstore.dto.responseDTO;

import org.ltclab.sb_bookstore.model.StoreElement;

import java.util.List;

public class BookResponseDTO implements StoreElement {
    private String title;
    private List<String> categories;
    private int publicationYear;
    private double price;
    private String authorFullName;
}
