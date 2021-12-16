package com.oched.booksprj.controllers;

import com.oched.booksprj.requests.ActionRequest;
import com.oched.booksprj.requests.EditBookRequest;
import com.oched.booksprj.requests.NewBookRequest;
import com.oched.booksprj.responses.BookInfoResponse;
import com.oched.booksprj.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/book-rest")
public class RestBookController {
    private final BookService bookService;

    @GetMapping("/list")
    public BookInfoResponse[] getAllBooks() {
        return this.bookService.getAll().toArray(new BookInfoResponse[0]);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addNewBook(final @Valid @RequestBody NewBookRequest request) {
        this.bookService.addBook(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateBook(final @Valid @RequestBody EditBookRequest request) {
        this.bookService.editBook(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBook(final @PathVariable Long id) {
        this.bookService.deleteBook(new ActionRequest(id));
        return ResponseEntity.ok().build();
    }
}
