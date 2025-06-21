package com.BookAPI.bookAPI;

import com.BookAPI.bookAPI.model.Book;
import com.BookAPI.bookAPI.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository repository;

    @Autowired
    ObjectMapper mapper;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = repository.save(new Book("Book 1", "Title 1", false, 2001));
    }

    @AfterEach
    void tearDown() {
        repository.delete(testBook.getTitle());
    }

    @Test
    void getAll_ShouldReturnThreeBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void getBookByTitle_ShouldReturnBook() throws Exception {
        mockMvc.perform(get("/api/books/" + testBook.getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(testBook.getTitle()))
                .andExpect(jsonPath("$.author").value(testBook.getAuthor()))
                .andExpect(jsonPath("$.year").value(testBook.getYear()));
    }

    @Test
    void getBookByTitle_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/books/" + "Book 2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveBook_ShouldReturnCreatedWithBook() throws Exception {
        Book book = repository.findAll().getFirst();
        book.setTitle("Test book");

        mockMvc.perform(post("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.year").value(book.getYear()));
    }

    @Test
    void deleteBook_ShouldDeleteBook_AndReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/books/" + testBook.getTitle()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deleteBook_ShouldNotDelete_AndReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/books/" + "Book 2"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void borrowBook_ShouldUpdateBook_AndReturnOk() throws Exception {
        mockMvc.perform(put("/api/books/" + testBook.getTitle() + "/borrow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(testBook.getTitle()))
                .andExpect(jsonPath("$.borrowed").value(true));

        Book book = repository.findByTitle(testBook.getTitle());

        assertEquals(book.getTitle(), testBook.getTitle());
        assertTrue(book.isBorrowed());
    }

    @Test
    void borrowBook_ShouldNotUpdate_AndReturnNotFound() throws Exception {
        mockMvc.perform(put("/api/books/" + "Book 2" + "/borrow"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnBook_ShouldUpdateBook_AndReturnOk() throws Exception {
        testBook.setBorrowed(true);
        testBook = repository.save(testBook);

        mockMvc.perform(put("/api/books/" + testBook.getTitle() + "/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(testBook.getTitle()))
                .andExpect(jsonPath("$.borrowed").value(false));

        Book book = repository.findByTitle(testBook.getTitle());

        assertEquals(book.getTitle(), testBook.getTitle());
        assertFalse(book.isBorrowed());
    }

    @Test
    void returnBook_ShouldNotUpdate_AndReturnNotFound() throws Exception {
        mockMvc.perform(put("/api/books/" + "Book 2" + "/return"))
                .andExpect(status().isNotFound());
    }
}
