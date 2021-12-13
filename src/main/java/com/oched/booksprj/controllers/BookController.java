package com.oched.booksprj.controllers;

import com.oched.booksprj.requests.ActionRequest;
import com.oched.booksprj.requests.EditBookRequest;
import com.oched.booksprj.requests.NewBookRequest;
import com.oched.booksprj.services.BookService;
import com.oched.booksprj.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RequiredArgsConstructor
@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final UserService userService;

    @GetMapping(value = "/add")
    private String getAddBookPage() {
        return "/books/addBook";
    }

    @PostMapping(value = "/add")
    public String addNewBook(final @ModelAttribute("request") NewBookRequest request) {
        this.bookService.addBook(request);

        return "redirect:/books/all";
    }

    @GetMapping("/all")
    public ModelAndView getAllBooks(final ModelAndView modelAndView) {
        if (this.userService.isAdmin()) {
            modelAndView.addObject("isAdmin", true);
        }
        modelAndView.addObject("list", this.bookService.getAll());
        modelAndView.setViewName("/books/allBooks");

        return modelAndView;
    }

    @GetMapping(value = "/edit")
    public ModelAndView getEditBookPage(ModelAndView modelAndView, @ModelAttribute("request") ActionRequest request) {
        modelAndView.addObject("book", this.bookService.getById(request));
        modelAndView.setViewName("/books/editBook");
        return modelAndView;
    }

    @PostMapping(value = "/edit")
    public String updateBook(final @ModelAttribute("request") EditBookRequest request) {
        this.bookService.editBook(request);
        return "redirect:/books/all";
    }

    @GetMapping(value = "/delete")
    public String deleteBook(@ModelAttribute("request") ActionRequest request) {
        this.bookService.deleteBook(request);
        return "redirect:/books/all";
    }
}
