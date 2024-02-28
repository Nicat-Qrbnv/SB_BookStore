package org.ltclab.sb_bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.ltclab.sb_bookstore.dto.AuthorDTO;
import org.ltclab.sb_bookstore.service.AuthorService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("book_store/author")
public class AuthorController {

    private final AuthorService as;

    @PostMapping("/add")
    public ResponseEntity<String> addAuthor (@RequestBody AuthorDTO authorDTO) {
        try {
            return ResponseEntity.ok(as.createAuthor(authorDTO));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Author already exists!");
        }
    }

    @GetMapping("/all")
    public List<AuthorDTO> getAllAuthors() {
        return as.getAllAuthors();
    }

    @GetMapping("/author{id}")
    public AuthorDTO getAuthorById (@PathVariable Long id) {
        return as.getAuthor(id);
    }

    @PutMapping("/update")
    public AuthorDTO updateAuthor(@RequestParam Long id, @RequestBody AuthorDTO newAuthor) {
        return as.updateAuthor(id, newAuthor);
    }

    @DeleteMapping("/delete{id}")
    public AuthorDTO deleteAuthor(@PathVariable Long id) {
        return as.deleteAuthor(id);
    }
}
