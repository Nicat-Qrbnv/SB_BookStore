package org.ltclab.sb_bookstore.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name = "category")
@Data
@RequiredArgsConstructor
public class Category {
    @Column (unique = true)
    private String categoryName;

    @ManyToMany (mappedBy = "categories", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private List<Book> booksOfCategory;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
