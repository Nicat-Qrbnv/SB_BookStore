package org.ltclab.sb_bookstore.service;

import lombok.RequiredArgsConstructor;

import org.ltclab.sb_bookstore.dto.requestDTO.AuthorRequestDTO;
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

    public String createAuthor(AuthorRequestDTO authorRequestDTO) {

        if (authorRepo.getAuthorByFullName(authorRequestDTO.getFullName()).isEmpty()) {
            Author author = mpr.map(authorRequestDTO, Author.class);
            authorRepo.save(author);
            return "created";
        } else {
            return "exists";
        }
    }

    public List<AuthorRequestDTO> getAllAuthors() {
        List<Author> authors = authorRepo.findAll();

        return authors.stream().map(a -> mpr.map(a, AuthorRequestDTO.class)).toList();
    }

    public AuthorRequestDTO getAuthor(long id) {
        Author author = authorRepo.findById(id).orElse(null);
        return mpr.map(author, AuthorRequestDTO.class);
    }

    public AuthorRequestDTO updateAuthor(Long id, AuthorRequestDTO newAuthor) {
        Author author = authorRepo.findById(id).orElse(null);
        if (author != null) {
            author = mpr.map(newAuthor, Author.class);
            author.setId(id);
            authorRepo.save(author);
            return mpr.map(author, AuthorRequestDTO.class);
        } else {
            return null;
        }
    }

    public AuthorRequestDTO deleteAuthor(Long id) {
        Author author = authorRepo.findById(id).orElse(null);
        if (author != null) {
            authorRepo.delete(author);
            return mpr.map(author, AuthorRequestDTO.class);
        } else {
            return null;
        }
    }

}
