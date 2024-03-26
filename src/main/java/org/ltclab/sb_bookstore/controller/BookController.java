package org.ltclab.sb_bookstore.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ltclab.sb_bookstore.dto.requestDTO.BookRequestDTO;
import org.ltclab.sb_bookstore.dto.responseDTO.forBook.BookResponseDTO;
import org.ltclab.sb_bookstore.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("book_store/book")
public class BookController {

    private final BookService bookSvc;

    @PostMapping("/add")
    public ResponseEntity<BookResponseDTO> addBook (@RequestBody BookRequestDTO bkDTO) {
        BookResponseDTO result = bookSvc.createBook(bkDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public List<BookRequestDTO> getAllBooks() {
        return bookSvc.getAllBooks();
    }

    @GetMapping("/bk{id}")
    public BookRequestDTO getBookById (@PathVariable Long id) {
        return bookSvc.getBook(id);
    }


    @PutMapping("/update")
    public BookRequestDTO updateBook(@RequestParam Long id, @RequestBody BookRequestDTO newBook) {
        return bookSvc.updateBook(id, newBook);
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        try {
            String result = bookSvc.deleteBook(id).toString();
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
