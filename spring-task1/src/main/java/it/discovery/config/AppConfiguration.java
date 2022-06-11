package it.discovery.config;

import it.discovery.repository.BookRepository;
import it.discovery.repository.DBBookRepository;
import it.discovery.repository.XmlBookRepository;
import it.discovery.service.BookService;
import it.discovery.service.MainBookService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration(proxyBeanMethods = false)
@PropertySource("application.properties")
public class AppConfiguration {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Qualifier("db")
    @Primary
    public BookRepository dbRepository() {
        return new DBBookRepository();
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Qualifier("xml")
    public BookRepository xmlRepository() {
        return new XmlBookRepository();
    }

    @Bean
    public BookService bookService(BookRepository bookRepository) {
        return new MainBookService(bookRepository);
    }
}
