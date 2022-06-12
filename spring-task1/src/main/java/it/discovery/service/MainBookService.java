package it.discovery.service;

import it.discovery.event.LogEvent;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
//@Slf4j TODO check Lombok error
//@Service
public class MainBookService implements BookService {
    private final BookRepository repository;

    private boolean cachingEnabled;

    private final Map<Integer, Book> bookCache = new ConcurrentHashMap<>();

    private final ApplicationEventPublisher publisher;


    public MainBookService(/*@Qualifier("xml") */BookRepository repository, ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
        System.out.println("Using repository " + repository.getClass());
    }

    @Scheduled(fixedDelay = 300)
    @Override
    public void checkBookStorage() {
        if (repository.findBooks().isEmpty()) {
            System.out.println("ALARM!: No books in the storage!");
        }
    }

    @PostConstruct
    public void init() {
    }

    @Override
    @Async
    public void saveBook(Book book) {
        repository.saveBook(book);

        if (cachingEnabled) {
            bookCache.put(book.getId(), book);
        }
        System.out.println("Current thread=" + Thread.currentThread().getName());
        publisher.publishEvent(new LogEvent("Object saved: " + book));
    }

    @Override
    public Book findBookById(int id) {
        if (cachingEnabled && bookCache.containsKey(id)) {
            return bookCache.get(id);
        }


        return repository.findBookById(id);
    }

    @Override
    public List<Book> findBooks() {
        return repository.findBooks();
    }
}
