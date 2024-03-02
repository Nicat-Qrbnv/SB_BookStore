package org.ltclab.sb_bookstore.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;
import java.util.Objects;

@Entity
@Table (name = "book",
        uniqueConstraints = @UniqueConstraint(columnNames = {"title", "author_id"}))
@Data
public class Book implements StoreElement{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (unique = true)
    private String title;
    private int publicationYear;
    private double price;
    @ManyToOne
    @JoinColumn(name = "author_id")
    @Cascade(CascadeType.SAVE_UPDATE)
    private Author author;

    @ManyToMany
    private List<Category> categories;

    public void addCategory (Category c) {
        categories.add(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }
}
