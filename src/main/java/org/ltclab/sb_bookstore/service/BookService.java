package org.ltclab.sb_bookstore.service;

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

        Author author = findAuthorOrCreate(bookRequestDTO);
        Book book = searchBook(bookRequestDTO, author);
        if (book == null) {
            book = mpr.map(bookRequestDTO, Book.class);
            book.setAuthor(author);
            book.setCategories(searchCategories(bookRequestDTO));
            log.info(searchCategories(bookRequestDTO).toString());
            bookRepo.save(book);
        }
        return mpr.map(book, BookResponseDTO.class);
    }

    public List<BookRequestDTO> getAllBooks() {
        List<Book> books = bookRepo.findAll();
        return books.stream().map(b -> mpr.map(b, BookRequestDTO.class)).toList();
    }

    public BookRequestDTO getBook(long id) {
        Book book = bookRepo.findBookById(id);
        return mpr.map(book, BookRequestDTO.class);
    }

    public BookRequestDTO updateBook(Long id, BookRequestDTO newBook) {
        Book book = bookRepo.findBookById(id);
        if (book != null) {
            book = mpr.map(newBook, Book.class);
            book.setId(id);
            Author author = findAuthorOrCreate(newBook);
            book.setAuthor(author);
            bookRepo.save(book);
        }
        return mpr.map(book, BookRequestDTO.class);
    }

    public void deleteBook(Long id) {
        bookRepo.deleteById(id);
    }

    //helper methods
    private Author findAuthorOrCreate(BookRequestDTO bookRequestDTO) {
        AuthorRequestDTO requestDTO = bookRequestDTO.getAuthorRequestDTO();
        Author author = authorRepo.findByFullName(requestDTO.getFullName());
        if (author == null) {
            author = mpr.map(requestDTO, Author.class);
            author = authorRepo.save(author);
        }
        return author;
    }

    private Book searchBook(BookRequestDTO bookRequestDTO, Author author) {
        return bookRepo.findBook(bookRequestDTO.getTitle(), author);
    }

    private List<Category> searchCategories(BookRequestDTO bookRequestDTO) {
        List<CategoryDTO> categoryDTOs = bookRequestDTO.getCategoryDTOs();
        log.info("DTOs: " + categoryDTOs);
        List<Category> categories = categoryDTOs.stream().map(c -> mpr.map(c, Category.class)).toList();
        log.info("entities: " + categories);
        for (Category c : categories) {
            Category cat = ctgryRepo.findCategory(c.getCategoryName());
            if (cat != null) {
                c.setId(cat.getId());
            } else {
                ctgryRepo.save(c);
            }
        }
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
