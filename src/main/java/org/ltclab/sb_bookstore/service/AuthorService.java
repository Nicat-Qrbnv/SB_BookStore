package org.ltclab.sb_bookstore.service;

import lombok.RequiredArgsConstructor;

import org.ltclab.sb_bookstore.dto.requestDTO.AuthorRequestDTO;
import org.ltclab.sb_bookstore.dto.responseDTO.forAuthor.AuthorResponseDTO;
import org.ltclab.sb_bookstore.model.Author;
import org.ltclab.sb_bookstore.repository.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorService {

    private final AuthorRepository authorRepo;

    private final ModelMapper mpr;

    public AuthorResponseDTO createAuthor(AuthorRequestDTO authorRequestDTO) {
        Author author = authorRepo.findByFullName(authorRequestDTO.getFullName());
        if (author == null) {
            author = mpr.map(authorRequestDTO, Author.class);
            authorRepo.save(author);
        }
        return mpr.map(author, AuthorResponseDTO.class);
    }

    public List<AuthorResponseDTO> getAllAuthors() {
        List<Author> authors = authorRepo.findAll();
        return authors.stream().map(a -> mpr.map(a, AuthorResponseDTO.class)).toList();
    }

    public AuthorResponseDTO getAuthor(long id) {
        Optional<Author> author = authorRepo.findById(id);
        return mpr.map(author, AuthorResponseDTO.class);
    }

    public AuthorResponseDTO updateAuthor(Long id, AuthorRequestDTO newAuthor) {
        Optional<Author> found = authorRepo.findById(id);
        found.ifPresent(f -> {
            mpr.map(newAuthor, f);
            f.setId(id);
        });
        try {
            found.ifPresent(authorRepo::save);
        } catch (DataIntegrityViolationException e) {
            return null;
        }

        return mpr.map(found, AuthorResponseDTO.class);
    }

    public void deleteAuthor(Long id) {
        authorRepo.deleteById(id);
    }
}
