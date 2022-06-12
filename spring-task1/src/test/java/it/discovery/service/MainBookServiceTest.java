package it.discovery.service;

import it.discovery.config.AppConfiguration;
import it.discovery.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(AppConfiguration.class)
@ActiveProfiles("dev")
class MainBookServiceTest {

    @Autowired
    BookService bookService;

    @Test
    void findBookById_identifierCorrect_success() {
        Book book = new Book();
        book.setName("Introduction into Spring");
        book.setPages(100);
        book.setYear(2016);
        bookService.saveBook(book);

        Book book2 = bookService.findBookById(book.getId());
        assertNotNull(book2);
        assertSame(book, book2);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void findBooks_oneBookCreated_success() {
        Book book = new Book();
        book.setName("Deep Dive into Spring");
        book.setPages(100);
        book.setYear(2022);
        bookService.saveBook(book);

        List<Book> books = bookService.findBooks();
        assertEquals(1, books.size());
    }
}