package com.oched.booksprj.controllers;

import com.oched.booksprj.requests.ActionRequest;
import com.oched.booksprj.requests.EditBookRequest;
import com.oched.booksprj.requests.EditUserRequest;
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

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @GetMapping(value = "/add")
    public ModelAndView getNewUserForm(ModelAndView modelAndView) {
        if(this.userService.isSuper()) {
            modelAndView.addObject("isSuper", true);
        }
        modelAndView.setViewName("/users/addUser");

        return modelAndView;
    }

    @PostMapping(value = "/add")
    public ModelAndView addNewUser(@Validated @ModelAttribute("request") NewUserRequest request, ModelAndView modelAndView) {
        modelAndView.setViewName("/users/newUser");
        modelAndView.addObject("user", this.userService.addNewUser(request));
        return modelAndView;
    }

    @GetMapping(value = "/all")
    public ModelAndView getUsersList(ModelAndView modelAndView) {
        modelAndView.addObject("list", this.userService.getUsersList());
        if (this.userService.isSuper()) {
            modelAndView.addObject("isSuper", true);
        }
        modelAndView.setViewName("/users/allUsers");

        return modelAndView;
    }

    @GetMapping(value = "/edit")
    public ModelAndView getEditUserPage(ModelAndView modelAndView, @ModelAttribute("request") ActionRequest request) {
        if (this.userService.isSuper()) {
            modelAndView.addObject("isSuper", true);
        }

        modelAndView.addObject("role", this.userService.getUserRole(request));
        modelAndView.addObject("user", this.userService.getById(request));
        modelAndView.setViewName("/users/editUser");
        return modelAndView;
    }

    @PostMapping(value = "/edit")
    public String updateUser(final @ModelAttribute("request") EditUserRequest request) {
        this.userService.editUser(request);
        return "redirect:/users/all";
    }

    @GetMapping(value = "/delete")
    public String deleteUser(@ModelAttribute("request") ActionRequest request) {
        this.userService.deleteUser(request);
        return "redirect:/users/all";
    }
}
