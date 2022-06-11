package it.discovery.config;

import it.discovery.event.EventBus;
import it.discovery.log.ConsoleLogger;
import it.discovery.log.FileLogger;
import it.discovery.log.Logger;
import it.discovery.repository.BookRepository;
import it.discovery.repository.DBBookRepository;
import it.discovery.repository.XmlBookRepository;
import it.discovery.service.BookService;
import it.discovery.service.MainBookService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@PropertySource("application.properties")
public class AppConfiguration {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Qualifier("db")
    @Profile("prod")
    //@Primary
    public BookRepository dbRepository() {
        return new DBBookRepository();
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Qualifier("xml")
    @Profile("dev")
    public BookRepository xmlRepository(Environment env) {
        return new XmlBookRepository(env.getRequiredProperty("xml.file"));
    }

    @Bean
    public BookService bookService(BookRepository bookRepository, ApplicationEventPublisher publisher) {
        return new MainBookService(bookRepository, publisher);
    }

    @Configuration
    public static class LogConfiguration {
        @Bean
        public Logger fileLogger() {
            return new FileLogger();
        }

        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)
        public Logger consoleLogger() {
            return new ConsoleLogger();
        }
    }

    @Configuration
    public static class EventConfiguration {
        @Bean
        public EventBus eventBus(List<Logger> loggers) {
            return new EventBus(loggers);
        }
    }
}
