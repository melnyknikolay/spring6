package it.discovery.loader;

import it.discovery.config.AppConfiguration;
import it.discovery.event.EventBus;
import it.discovery.model.Book;
import it.discovery.proxy.MeasurementProxy;
import it.discovery.service.BookService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class SpringStarter {
    public static void main(String[] args) throws InterruptedException {
        try (var context =
                     new AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("dev");
            context.register(AppConfiguration.class);
            context.refresh();

            var service = context.getBean(BookService.class);
            var handler = new MeasurementProxy(service);
            var proxiedService = (BookService) Proxy.newProxyInstance(BookService.class.getClassLoader(),
                    new Class[]{BookService.class}, handler);

            EventBus eventBus = context.getBean(EventBus.class);
            System.out.println("Event bus class: " + eventBus.getClass());

            Book book = new Book();
            book.setName("Introduction into Spring");
            book.setPages(100);
            book.setYear(2016);
            service.saveBook(book);

            var books = proxiedService.findBooks();
            System.out.println(books);
            System.out.println("Total bean count = " + context.getBeanDefinitionCount());
            System.out.println("Bean identifiers: " + Arrays.toString(context.getBeanDefinitionNames()));

            Thread.sleep(1000);
        }

    }

}
