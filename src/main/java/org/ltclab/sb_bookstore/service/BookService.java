package org.ltclab.sb_bookstore.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ltclab.sb_bookstore.dto.BookDTO;
import org.ltclab.sb_bookstore.model.Author;
import org.ltclab.sb_bookstore.model.Book;
import org.ltclab.sb_bookstore.model.Category;
import org.ltclab.sb_bookstore.repository.AuthorRepository;
import org.ltclab.sb_bookstore.repository.BookRepository;
import org.ltclab.sb_bookstore.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;
    private final CategoryRepository ctgryRepo;

    private final ModelMapper mpr;

    public String createBook(BookDTO bookDTO) {

        Author author = getOrCreateAuthor(bookDTO);
        Book book = searchBook(bookDTO, author);
        if (book != null) {
            throw new DataIntegrityViolationException("Book already exists!");
        }
        book = mpr.map(bookDTO, Book.class);
        book.setAuthor(author);

        book.setCategories(searchCategories(bookDTO));
        bookRepo.save(book);

        return "Book created:\n\t" + mpr.map(book, BookDTO.class);
    }

    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepo.findAll();

        return books.stream().map(b -> mpr.map(b, BookDTO.class)).toList();
    }

    public BookDTO getBook(long id) {
        Book book = bookRepo.findById(id).orElseThrow();
        return mpr.map(book, BookDTO.class);
    }

    public BookDTO updateBook(Long id, BookDTO newBook) {

        Book book = bookRepo.findById(id).orElse(null);
        if (book != null) {

            book = mpr.map(newBook, Book.class);
            book.setId(id);
            Author author = getOrCreateAuthor(newBook);
            book.setAuthor(author);
            bookRepo.save(book);
        }
        return mpr.map(book, BookDTO.class);
    }

    public BookDTO deleteBook(Long id) {
        log.info("Searching for book with id: " + id);
        Book book = bookRepo.findById(id).orElse(null);
        if (book != null) {
            log.info("Book found: " + book);
            bookRepo.delete(book);
            log.info("Deleted");
            return mpr.map(book, BookDTO.class);
        } else {
            log.error("Book not found!");
            throw new EntityNotFoundException("Book does not exist!");
        }
    }

    private Book searchBook(BookDTO bookDTO, Author author) {
        log.info("Searching for Book {title: %s | author: %s}".formatted(bookDTO.getTitle(), bookDTO.getAuthorFullName()));
        return bookRepo.findByTitleAndAuthor(bookDTO.getTitle(), author).orElse(null);
    }

    private List<Category> searchCategories(BookDTO bookDTO) {
        List<Category> categories = bookDTO.getCategories().stream().map(Category::new).toList();
        return ctgryRepo.saveAll(categories);
    }

    private Author getOrCreateAuthor(BookDTO bookDTO) {
        log.info("Searching for Author {name: %s}".formatted(bookDTO.getAuthorFullName()));
        Author author = authorRepo.getAuthorByFullName(bookDTO.getAuthorFullName()).orElse(null);
        if (author == null) {
            log.info("Author is not found. Creating author");
            author = mpr.map(bookDTO, Author.class);
            log.info("Author mapped");
            authorRepo.save(author);
            log.info(author + " is saved");
        }
        return author;
    }
}
