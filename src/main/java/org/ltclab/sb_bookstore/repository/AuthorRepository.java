package org.ltclab.sb_bookstore.repository;

import org.ltclab.sb_bookstore.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    @Override
    List<Author> findAll();

    Optional<Author> findById(Long id);

    Author findByFullName(String fullName);

    @Override
    void deleteById(Long aLong);
}
