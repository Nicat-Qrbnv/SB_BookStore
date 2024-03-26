package org.ltclab.sb_bookstore.repository;

import org.ltclab.sb_bookstore.model.Author;
import org.ltclab.sb_bookstore.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    @Override
    List<Book> findAll();

    Optional<Book> findById(Long id);

    Optional<Book> findByTitleAndAuthor(String title, Author author);
}
