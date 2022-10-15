package com.yaniv.bookshelf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    public AuthController(DaoAuthenticationProvider daoAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        return new ModelAndView("login");
    }
}
