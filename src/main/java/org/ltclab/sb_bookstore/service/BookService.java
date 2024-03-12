package org.ltclab.sb_bookstore.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ltclab.sb_bookstore.client.GoodReadsClient;
import org.ltclab.sb_bookstore.dto.requestDTO.BookRequestDTO;
import org.ltclab.sb_bookstore.dto.responseDTO.BookResponseDTO;
import org.ltclab.sb_bookstore.model.Author;
import org.ltclab.sb_bookstore.model.Book;
import org.ltclab.sb_bookstore.model.Category;
import org.ltclab.sb_bookstore.repository.AuthorRepository;
import org.ltclab.sb_bookstore.repository.BookRepository;
import org.ltclab.sb_bookstore.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    private static final String API_HOST_HEADER_VALUE = "goodreads-books.p.rapidapi.com";
    private static final String API_KEY = "43a2b5c1e7msh8acac68a5f0123bp16ce56jsn7851bc2a7814";

    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;
    private final CategoryRepository ctgryRepo;
    private final GoodReadsClient grc;

    private final ModelMapper mpr;

    public String createBook(BookRequestDTO bookRequestDTO) {

        Author author = getOrCreateAuthor(bookRequestDTO);
        Book book = searchBook(bookRequestDTO, author);
        if (book != null) {
            throw new DataIntegrityViolationException("Book already exists!");
        }
        book = mpr.map(bookRequestDTO, Book.class);
        book.setAuthor(author);
        if (book.getPublicationYear() == 0) {
            int foundYear = findPublicationYear(bookRequestDTO);
            if (foundYear > 0) {
                book.setPublicationYear(foundYear);
            } else {
                book.setPublicationYear(Year.now().getValue());
            }
        }

        book.setCategories(searchCategories(bookRequestDTO));
        bookRepo.save(book);

        return "Book created:\n\t" + mpr.map(book, BookRequestDTO.class);
    }

    public List<BookRequestDTO> getAllBooks() {
        List<Book> books = bookRepo.findAll();

        return books.stream().map(b -> mpr.map(b, BookRequestDTO.class)).toList();
    }

    public BookRequestDTO getBook(long id) {
        Book book = bookRepo.findById(id).orElseThrow();
        return mpr.map(book, BookRequestDTO.class);
    }

    public BookRequestDTO updateBook(Long id, BookRequestDTO newBook) {

        Book book = bookRepo.findById(id).orElse(null);
        if (book != null) {

            book = mpr.map(newBook, Book.class);
            book.setId(id);
            Author author = getOrCreateAuthor(newBook);
            book.setAuthor(author);
            bookRepo.save(book);
        }
        return mpr.map(book, BookRequestDTO.class);
    }

    public BookRequestDTO deleteBook(Long id) {
        log.info("Searching for book with id: " + id);
        Book book = bookRepo.findById(id).orElse(null);
        if (book != null) {
            log.info("Book found: " + book);
            bookRepo.delete(book);
            log.info("Deleted");
            return mpr.map(book, BookRequestDTO.class);
        } else {
            log.error("Book not found!");
            throw new EntityNotFoundException("Book does not exist!");
        }
    }

    private Book searchBook(BookRequestDTO bookRequestDTO, Author author) {
        log.info("Searching for Book {title: %s | author: %s}".formatted(bookRequestDTO.getTitle(), bookRequestDTO.getAuthorFullName()));
        return bookRepo.findByTitleAndAuthor(bookRequestDTO.getTitle(), author).orElse(null);
    }

    private List<Category> searchCategories(BookRequestDTO bookRequestDTO) {
        List<Category> categories = bookRequestDTO.getCategories().stream().map(Category::new).toList();
        return ctgryRepo.saveAll(categories);
    }

    private Author getOrCreateAuthor(BookRequestDTO bookRequestDTO) {
        log.info("Searching for Author {name: %s}".formatted(bookRequestDTO.getAuthorFullName()));
        Author author = authorRepo.getAuthorByFullName(bookRequestDTO.getAuthorFullName()).orElse(null);
        if (author == null) {
            log.info("Author is not found. Creating author");
            author = mpr.map(bookRequestDTO, Author.class);
            log.info("Author mapped");
            authorRepo.save(author);
            log.info(author + " is saved");
        }
        return author;
    }

    private int findPublicationYear (BookRequestDTO bookDTO) {

        BookResponseDTO bookResponseDTO = grc.searchBookInGoodReads(API_HOST_HEADER_VALUE, API_KEY, bookDTO.getTitle()).get(0);
        if (bookResponseDTO != null) {
            return bookResponseDTO.getPublicationYear();
        } else {
            return -1;
        }
    }
}
