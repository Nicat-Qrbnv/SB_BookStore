package org.ltclab.sb_bookstore.repository;

import feign.Param;
import org.ltclab.sb_bookstore.model.Author;
import org.ltclab.sb_bookstore.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    @Override
    List<Book> findAll();

    Book findBookById(Long id);

    @Query ("SELECT b FROM Book b WHERE b.title = :title AND b.author = :author")
    Book findBook (@Param("title") String title, @Param("author") Author author);
}
