package com.BookAPI.bookAPI.controller;

import com.BookAPI.bookAPI.model.Book;
import com.BookAPI.bookAPI.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/books")
public class BookController {
    @Autowired
    BookService service;

    @GetMapping
    public ResponseEntity<List<Book>> getAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{title}")
    public ResponseEntity<Book> getBookByTitle(@PathVariable String title){
        Book book = service.findByTitle(title);
        if (book == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody Book book){
        Book savedBook = service.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteBook(@PathVariable String title){
        service.deleteBook(title);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{title}/borrow")
    public ResponseEntity<Book> borrowBook(@PathVariable String title){
        Book book = service.borrowBook(title);

        if (book == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{title}/return")
    public ResponseEntity<Book> returnBook(@PathVariable String title){
        Book book = service.returnBook(title);

        if (book == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }
}
