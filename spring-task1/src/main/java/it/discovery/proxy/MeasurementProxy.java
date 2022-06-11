package it.discovery.proxy;

import it.discovery.service.BookService;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class MeasurementProxy implements InvocationHandler {

    private final BookService bookService;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long currentTime = System.nanoTime();

        try {
            return method.invoke(bookService, args);
        } finally {
            currentTime = (System.nanoTime() - currentTime) / 1_000;
            System.out.println("Execution time: " + currentTime + " (ps)");
        }

    }
}
