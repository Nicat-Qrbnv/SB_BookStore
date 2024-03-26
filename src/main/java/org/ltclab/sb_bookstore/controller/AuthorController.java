package org.ltclab.sb_bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.ltclab.sb_bookstore.dto.requestDTO.AuthorRequestDTO;
import org.ltclab.sb_bookstore.dto.responseDTO.forAuthor.AuthorResponseDTO;
import org.ltclab.sb_bookstore.service.AuthorService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("book_store/author")
public class AuthorController {

    private final AuthorService as;

    @PostMapping("/add")
    public ResponseEntity<AuthorResponseDTO> addAuthor(@RequestBody AuthorRequestDTO authorRequestDTO) {
        AuthorResponseDTO result = as.createAuthor(authorRequestDTO);
        return new ResponseEntity<>(result, HttpStatusCode.valueOf(202));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        List<AuthorResponseDTO> result = as.getAllAuthors();
        return new ResponseEntity<>(result, HttpStatusCode.valueOf(202));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable Long id) {
        var result = as.getAuthor(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(@RequestParam Long id, @RequestBody AuthorRequestDTO newAuthor) {
        AuthorResponseDTO result = as.updateAuthor(id, newAuthor);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        as.deleteAuthor(id);
    }
}
