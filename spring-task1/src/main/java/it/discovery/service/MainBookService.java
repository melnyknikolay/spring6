package it.discovery.service;

import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import it.discovery.repository.DBBookRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class MainBookService {
    private final BookRepository repository =
            new DBBookRepository();

	private boolean cachingEnabled;

	private final Map<Integer, Book> bookCache = new ConcurrentHashMap<>();


	public MainBookService() {
		System.out.println("Using db repository");
	}

	public void saveBook(Book book) {
		repository.saveBook(book);

		if (cachingEnabled) {
			bookCache.put(book.getId(), book);
		}
	}

	public Book findBookById(int id) {
		if (cachingEnabled && bookCache.containsKey(id)) {
			return bookCache.get(id);
		}


		return repository.findBookById(id);
	}

	public List<Book> findBooks() {
		return repository.findBooks();
	}
}
