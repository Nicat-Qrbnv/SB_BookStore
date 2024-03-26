package org.ltclab.sb_bookstore.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.ltclab.sb_bookstore.client.GoodReadsClient;
import org.ltclab.sb_bookstore.dto.CategoryDTO;
import org.ltclab.sb_bookstore.dto.requestDTO.AuthorRequestDTO;
import org.ltclab.sb_bookstore.dto.requestDTO.BookRequestDTO;
import org.ltclab.sb_bookstore.dto.responseDTO.forBook.BookResponseDTO;
import org.ltclab.sb_bookstore.model.Author;
import org.ltclab.sb_bookstore.model.Book;
import org.ltclab.sb_bookstore.model.Category;
import org.ltclab.sb_bookstore.repository.AuthorRepository;
import org.ltclab.sb_bookstore.repository.BookRepository;
import org.ltclab.sb_bookstore.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    //Static fields for methods calling Good Reads API
    @Value("${goodReadsApiHost}")
    private static String HOST;
    @Value("${goodReadsApiKey}")
    private static String KEY;

    //Repositories
    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;
    private final CategoryRepository ctgryRepo;

    //injected feign client
    private final GoodReadsClient grc;

    //mapper
    private final ModelMapper mpr;

    //methods
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {

        Author author = findAuthor(bookRequestDTO);
        Book book = searchBook(bookRequestDTO, author);
        if (book == null) {
            book = mpr.map(bookRequestDTO, Book.class);
            log.info(book.toString());
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
        }

        return mpr.map(book, BookResponseDTO.class);
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
            Author author = findAuthor(newBook);
            book.setAuthor(author);
            bookRepo.save(book);
        }
        return mpr.map(book, BookRequestDTO.class);
    }

    public BookRequestDTO deleteBook(Long id) {
        Book book = bookRepo.findById(id).orElse(null);
        if (book != null) {
            bookRepo.delete(book);
            return mpr.map(book, BookRequestDTO.class);
        } else {
            throw new EntityNotFoundException("Book does not exist!");
        }
    }

    //helper methods

    private Author findAuthor(BookRequestDTO bookRequestDTO) {
        AuthorRequestDTO authorRequestDTO = bookRequestDTO.getAuthorRequestDTO();
        return authorRepo.findByFullName(authorRequestDTO.getFullName());
    }

    private Book searchBook(BookRequestDTO bookRequestDTO, Author author) {
        return bookRepo.findByTitleAndAuthor(bookRequestDTO.getTitle(), author).orElse(null);
    }

    private List<Category> searchCategories(BookRequestDTO bookRequestDTO) {
        List<CategoryDTO> categoryDTOs = bookRequestDTO.getCategoryDTOs();
        List<Category> categories = new ArrayList<>();
        mpr.map(categoryDTOs, categories);
        categories.stream().filter(c -> !ctgryRepo.existsByCategoryName(c.getCategoryName())).forEach(ctgryRepo::save);
        return categories;
    }

    private int findNumberOfPages() throws IOException {
        Document doc = Jsoup.connect("https://www.goodreads.com/book/show/40603587-the-last-wish?ac=1&from_search=true&qid=SnTfJjJGRJ&rank=1").get();
        Elements el = doc.select("#__next > div.PageFrame.PageFrame--siteHeaderBanner > main > div.BookPage__gridContainer > div.BookPage__rightColumn > div.BookPage__mainContent > div.BookPageMetadataSection > div.BookDetails > div > span:nth-child(1) > span > div > p:nth-child(1)");
        String pages = el.text().replaceAll("[^0-9]", "");
        return Integer.parseInt(pages);
    }

    private int findPublicationYear(BookRequestDTO bookDTO) {

        BookResponseDTO bookResponseDTO = grc.searchBookInGoodReads(HOST, KEY, bookDTO.getTitle()).get(0);
        if (bookResponseDTO != null) {
            return bookResponseDTO.getPublicationYear();
        } else {
            return -1;
        }
    }
}
