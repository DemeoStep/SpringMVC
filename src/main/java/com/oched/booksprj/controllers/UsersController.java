package com.oched.booksprj.controllers;

import com.oched.booksprj.requests.NewUserRequest;
import com.oched.booksprj.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;


@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @GetMapping(value = "/add")
    public String getNewUserForm() {
        return "/users/addUser";
    }

    @PostMapping(value = "/add")
    public ModelAndView addNewUser(@Validated @ModelAttribute("request") NewUserRequest request, ModelAndView modelAndView) {
        modelAndView.setViewName("/users/newUser");
        modelAndView.addObject("user", this.userService.addNewUser(request));

        return modelAndView;
    }

    @GetMapping(value = "/all")
    public ModelAndView getUsersList(ModelAndView modelAndView) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int roles = user.getAuthorities().size();

        modelAndView.addObject("list", this.userService.getUsersList());
        if (roles > 2) {
            modelAndView.addObject("roles", true);
        }
        modelAndView.setViewName("/users/allUsers");

        return modelAndView;
    }
}
