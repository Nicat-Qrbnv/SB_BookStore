package org.ltclab.sb_bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.ltclab.sb_bookstore.dto.AuthorDTO;
import org.ltclab.sb_bookstore.service.AuthorService;
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
        String result = as.createAuthor(authorDTO);
        if (result.equals("created")){
            return ResponseEntity.ok("Author: %s is added!".formatted(authorDTO.getFullName()));
        } else {
            return ResponseEntity.ok("Author: %s exists!".formatted(authorDTO.getFullName()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<StringBuilder> getAllAuthors() {
        List<AuthorDTO> allAuthors = as.getAllAuthors();
        if (allAuthors.isEmpty()) {
            return ResponseEntity.ok(new StringBuilder("No author found!"));
        } else {
            StringBuilder str = new StringBuilder("Found Authors:\n");
            allAuthors.forEach(a -> str.append(a.toString().indent(4)));
            return ResponseEntity.ok (str);
        }
    }

    @GetMapping("/only{id}")
    public String getAuthorById (@PathVariable Long id) {
        AuthorDTO authorDTO = as.getAuthor(id);
        if (authorDTO == null) {
            return "No author with %d ID is present!".formatted(id);
        } else {
            return authorDTO.toString();
        }
    }

    @PutMapping("/update")
    public String updateAuthor(@RequestParam Long id, @RequestBody AuthorDTO newAuthor) {
        AuthorDTO authorDTO = as.updateAuthor(id, newAuthor);
        if (authorDTO == null) {
            return "No author with %d ID is present!".formatted(id);
        } else {
            return authorDTO.toString();
        }
    }

    @DeleteMapping("/delete{id}")
    public String deleteAuthor(@PathVariable Long id) {
        AuthorDTO authorDTO = as.deleteAuthor(id);
        if (authorDTO == null) {
            return "No author with %d ID is present!".formatted(id);
        } else {
            return authorDTO.toString();
        }
    }
}
