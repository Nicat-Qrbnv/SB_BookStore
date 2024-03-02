package org.ltclab.sb_bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.ltclab.sb_bookstore.dto.requestDTO.AuthorRequestDTO;
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
    public ResponseEntity<String> addAuthor (@RequestBody AuthorRequestDTO authorRequestDTO) {
        String result = as.createAuthor(authorRequestDTO);
        if (result.equals("created")){
            return ResponseEntity.ok("Author: %s is added!".formatted(authorRequestDTO.getFullName()));
        } else {
            return ResponseEntity.ok("Author: %s exists!".formatted(authorRequestDTO.getFullName()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<StringBuilder> getAllAuthors() {
        List<AuthorRequestDTO> allAuthors = as.getAllAuthors();
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
        AuthorRequestDTO authorRequestDTO = as.getAuthor(id);
        if (authorRequestDTO == null) {
            return "No author with %d ID is present!".formatted(id);
        } else {
            return authorRequestDTO.toString();
        }
    }

    @PutMapping("/update")
    public String updateAuthor(@RequestParam Long id, @RequestBody AuthorRequestDTO newAuthor) {
        AuthorRequestDTO authorRequestDTO = as.updateAuthor(id, newAuthor);
        if (authorRequestDTO == null) {
            return "No author with %d ID is present!".formatted(id);
        } else {
            return authorRequestDTO.toString();
        }
    }

    @DeleteMapping("/delete{id}")
    public String deleteAuthor(@PathVariable Long id) {
        AuthorRequestDTO authorRequestDTO = as.deleteAuthor(id);
        if (authorRequestDTO == null) {
            return "No author with %d ID is present!".formatted(id);
        } else {
            return authorRequestDTO.toString();
        }
    }
}
