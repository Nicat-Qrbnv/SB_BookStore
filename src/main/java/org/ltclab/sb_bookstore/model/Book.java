package org.ltclab.sb_bookstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;

@Entity
@Table (name = "book",
        uniqueConstraints = @UniqueConstraint(columnNames = {"title", "author_id"}))
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (unique = true)
    private String title;
    private int publicationYear;
    private double price;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    @Cascade(CascadeType.DETACH)
    @JsonBackReference
    private Author author;

    @ManyToMany
    @JsonManagedReference
    private List<Category> categories;
}
