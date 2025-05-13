package com.BookAPI.bookAPI.service;

import com.BookAPI.bookAPI.model.Book;
import com.BookAPI.bookAPI.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    BookRepository repository;

    public List<Book> findAll(){
        return repository.findAll();
    }

    public Book findByTitle(String title){
        return repository.findByTitle(title);
    }

    public Book addBook(Book book){
        return repository.save(book);
    }

    public void deleteBook(String title){
        repository.delete(title);
    }
}
