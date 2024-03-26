package org.ltclab.sb_bookstore.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ltclab.sb_bookstore.dto.requestDTO.AuthorRequestDTO;
import org.ltclab.sb_bookstore.dto.responseDTO.forAuthor.AuthorResponseDTO;
import org.ltclab.sb_bookstore.model.Author;
import org.ltclab.sb_bookstore.repository.AuthorRepository;
import org.ltclab.sb_bookstore.service.AuthorService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestAuthorService {
    private AuthorRequestDTO requestDTO;
    private AuthorResponseDTO responseDTO;
    private Author entity;

    @InjectMocks
    AuthorService authorSvc;

    @Mock
    AuthorRepository authorRepo;
    @Mock
    ModelMapper mpr;

    @BeforeEach
    void init() {
        requestDTO = new AuthorRequestDTO();
        requestDTO.setFullName("Author");
        responseDTO = new AuthorResponseDTO();
        responseDTO.setFullName("Author");

        entity = new Author();
        entity.setFullName(requestDTO.getFullName());
    }

    @Test
    void testCreateAuthor_IfAuthorNotExist() {
        when(authorRepo.findByFullName("Author")).thenReturn(null);
        when(mpr.map(requestDTO, Author.class)).thenReturn(entity);
        when(authorRepo.save(entity)).thenReturn(entity);
        when(mpr.map(entity, AuthorResponseDTO.class)).thenReturn(responseDTO);

        var response = authorSvc.createAuthor(requestDTO);
        assertEquals(responseDTO.getFullName(), response.getFullName());
    }

    @Test
    void testCreateAuthor_IfAuthorExists () {
        when(authorRepo.findByFullName("Author")).thenReturn(entity);
        when(mpr.map(entity, AuthorResponseDTO.class)).thenReturn(responseDTO);

        var response = authorSvc.createAuthor(requestDTO);
        assertEquals(responseDTO.getFullName(), response.getFullName());
    }

    @Test
    void testGetAllAuthors() {
        Author a1 = new Author(); a1.setFullName("Author 1");
        Author a2 = new Author(); a2.setFullName("Author 2");

        when(authorRepo.findAll()).thenReturn(List.of(a1, a2));

        assertEquals(2, authorSvc.getAllAuthors().size());
    }

    @Test
    void testGetAuthor_IfAuthorNotExist () {
        long id = 0;
        when(authorRepo.findById(id)).thenReturn(Optional.empty());
        when(mpr.map(Optional.empty(), AuthorResponseDTO.class)).thenReturn(null);
        assertNull(authorSvc.getAuthor(id));
    }

    @Test
    void testGetAuthor_IfAuthorExists () {
        long id = 1;
        when(authorRepo.findById(id)).thenReturn(Optional.ofNullable(entity));
        when(mpr.map(Optional.ofNullable(entity), AuthorResponseDTO.class)).thenReturn(responseDTO);
        assertEquals(responseDTO.getFullName(), authorSvc.getAuthor(id).getFullName());
    }

    @Test
    void testUpdateAuthor_IfAuthorNotExist () {
        long id = 0;
        Optional<Author> empty = Optional.empty();
        when(authorRepo.findById(id)).thenReturn(empty);

        assertNull(authorSvc.updateAuthor(id, requestDTO));
    }

    @Test
    void testUpdateAuthor_IfAuthorExists () {
        long id = 1;
        Optional<Author> found = Optional.ofNullable(entity);
        when(authorRepo.findById(id)).thenReturn(found);
        doNothing().when(mpr).map(requestDTO, entity);
        when(authorRepo.save(entity)).thenReturn(entity);
        when(mpr.map(found, AuthorResponseDTO.class)).thenReturn(responseDTO);

        assertEquals(responseDTO.getFullName(), authorSvc.updateAuthor(id, requestDTO).getFullName());
    }

    @Test
    void testUpdateAuthor_IfAuthorExistsButRequestIsDuplicated () {
        long id = 1;
        Optional<Author> found = Optional.ofNullable(entity);
        when(authorRepo.findById(id)).thenReturn(found);
        doNothing().when(mpr).map(requestDTO, entity);
        when(authorRepo.save(entity)).thenThrow(DataIntegrityViolationException.class);

        assertEquals(requestDTO.getFullName(), found.get().getFullName());
        assertNull(authorSvc.updateAuthor(id, requestDTO));
    }
}