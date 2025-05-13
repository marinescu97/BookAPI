package com.BookAPI.bookAPI.component;

import com.BookAPI.bookAPI.model.Book;
import com.BookAPI.bookAPI.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
//    @Autowired
//    BookService service;

    private final BookService service;

    @Autowired
    public DataInitializer(BookService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception{
        service.addBook(new Book("Clean code", "Martin", false, 2008));
        service.addBook(new Book("Clean", "Fake Martin", false, 2010));
        System.out.println("books were added: " + service.findAll().size());
    }
}
