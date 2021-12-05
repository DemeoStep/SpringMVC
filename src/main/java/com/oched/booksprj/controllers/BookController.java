package com.oched.booksprj.controllers;

import com.oched.booksprj.requests.AddBookRequest;
import com.oched.booksprj.requests.DeleteOrEditBookRequest;
import com.oched.booksprj.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RequiredArgsConstructor
@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping(value = "/add")
    private String getAddBookPage() {
        return "/books/addBook";
    }

    @PostMapping(value = "/add")
    public String addNewBook(final @ModelAttribute("request") AddBookRequest request) {
        this.bookService.addBook(request);

        return "redirect:/books/all";
    }

    @GetMapping(value = "/edit")
    public ModelAndView getEditBookPage(ModelAndView modelAndView, int id) {
        modelAndView.addObject("book", this.bookService.getById(id));
        modelAndView.setViewName("/books/editBook");
        return modelAndView;
    }

    @PostMapping(value = "/edit")
    public String updateBook(final @ModelAttribute("request") AddBookRequest request) {
        this.bookService.editBook(request);
        return "redirect:/books/all";
    }

    @PostMapping(value = "/delete")
    public String deleteBook(final @ModelAttribute("request") DeleteOrEditBookRequest request) {
        this.bookService.deleteBook(request);
        return "redirect:/books/all";
    }

    @GetMapping("/all")
    public ModelAndView getAllBooks(final ModelAndView modelAndView) {
        modelAndView.addObject("list", this.bookService.getAll());
        modelAndView.setViewName("/books/allBooks");

        return modelAndView;
    }
}
