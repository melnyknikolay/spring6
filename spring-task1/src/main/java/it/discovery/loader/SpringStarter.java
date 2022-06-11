package it.discovery.loader;

import it.discovery.model.Book;
import it.discovery.repository.DBBookRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class SpringStarter {
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("it.discovery")) {

			DBBookRepository repository = null; //TODO Load from context

			Book book = new Book();
			book.setName("Introduction into Spring");
			book.setPages(100);
			book.setYear(2016);
			repository.saveBook(book);

			List<Book> books = repository.findBooks();
			System.out.println(books);
		}

	}

}
