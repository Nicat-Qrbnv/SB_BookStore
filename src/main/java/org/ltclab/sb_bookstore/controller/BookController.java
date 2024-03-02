package org.ltclab.sb_bookstore.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ltclab.sb_bookstore.dto.requestDTO.BookRequestDTO;
import org.ltclab.sb_bookstore.service.BookService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("book_store/book")
public class BookController {

    private final BookService bs;

    @PostMapping("/add")
    public ResponseEntity<String> addBook (@RequestBody BookRequestDTO bkDTO) {
        try {
            String result = bs.createBook(bkDTO);
            return ResponseEntity.ok(result);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public List<BookRequestDTO> getAllBooks() {
        return bs.getAllBooks();
    }

    @GetMapping("/bk{id}")
    public BookRequestDTO getBookById (@PathVariable Long id) {
        return bs.getBook(id);
    }


    @PutMapping("/update")
    public BookRequestDTO updateBook(@RequestParam Long id, @RequestBody BookRequestDTO newBook) {
        return bs.updateBook(id, newBook);
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        try {
            String result = bs.deleteBook(id).toString();
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
