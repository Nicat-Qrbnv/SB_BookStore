package org.ltclab.sb_bookstore.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
@Data
public class Author implements StoreElement{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (unique = true)
    private String fullName;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("\nBooks:\n");
        books.forEach(b -> str.append(b.getTitle().indent(4)));
        return "Author: " + fullName + str;
    }
}
