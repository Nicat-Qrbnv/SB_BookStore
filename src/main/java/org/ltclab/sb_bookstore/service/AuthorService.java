package org.ltclab.sb_bookstore.service;

import lombok.RequiredArgsConstructor;

import org.ltclab.sb_bookstore.dto.AuthorDTO;
import org.ltclab.sb_bookstore.model.Author;
import org.ltclab.sb_bookstore.repository.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorService {

    private final AuthorRepository authorRepo;

    private final ModelMapper mpr;

    public String createAuthor(AuthorDTO authorDTO) {

        Author author = mpr.map(authorDTO, Author.class);
        authorRepo.save(author);

        return "Author named: %s is created!".formatted(author.getFullName());
    }

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepo.findAll();

        return authors.stream().map(a -> mpr.map(a, AuthorDTO.class)).toList();
    }

    public AuthorDTO getAuthor (long id) {
        Author author = authorRepo.findById(id).orElseThrow();
        return mpr.map(author, AuthorDTO.class);
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO newAuthor) {
        Author author = authorRepo.findById(id).orElse(null);
        if (author != null) {
            author = mpr.map(newAuthor, Author.class);
            author.setId(id);
            authorRepo.save(author);
        }
        return mpr.map(author, AuthorDTO.class);
    }

    public AuthorDTO deleteAuthor(Long id) {
        Author author = authorRepo.findById(id).orElseThrow();
        authorRepo.delete(author);
        return mpr.map(author, AuthorDTO.class);
    }

}
