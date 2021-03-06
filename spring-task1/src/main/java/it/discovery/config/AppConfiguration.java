package it.discovery.config;

import it.discovery.bpp.CustomInitBeanPostProcessor;
import it.discovery.bpp.DebugBeanPostProcessor;
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
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@PropertySource("application.properties")
@EnableAsync
@EnableScheduling
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
        @Lazy
        public EventBus eventBus(List<Logger> loggers) {
            return new EventBus(loggers);
        }
    }

    @Configuration
    public static class BPPConfiguration {
        @Bean
        public BeanPostProcessor debugProcessor() {
            return new DebugBeanPostProcessor();
        }

        @Bean
        public BeanPostProcessor customInitProcessor(ApplicationContext applicationContext) {
            return new CustomInitBeanPostProcessor(applicationContext);
        }
    }
}
