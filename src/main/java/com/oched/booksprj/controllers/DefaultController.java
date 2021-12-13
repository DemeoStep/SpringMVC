package com.oched.booksprj.controllers;

import com.oched.booksprj.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class DefaultController {
    private final UserService userService;

    @GetMapping("/hello/{name}")
    public ModelAndView getHello(@PathVariable String name, ModelAndView modelAndView) {
        modelAndView.addObject(name);
        modelAndView.setViewName("greeting");

        return modelAndView;
    }

    @GetMapping(value = "/")
    public ModelAndView gotoMain(ModelAndView modelAndView) {
        if (this.userService.isAdmin()) {
            modelAndView.addObject("isAdmin", true);
            modelAndView.addObject("isUser", true);
        } else if (this.userService.isUser()) {
            modelAndView.addObject("isUser", true);
        }
        modelAndView.setViewName("index");
        return modelAndView;
    }

}
