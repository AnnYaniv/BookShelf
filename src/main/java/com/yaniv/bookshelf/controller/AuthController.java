package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.VisitorDto;
import com.yaniv.bookshelf.mapper.VisitorMapper;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.service.VisitorService;
import com.yaniv.bookshelf.service.VisitorValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final VisitorService visitorService;

    @Autowired
    public AuthController(DaoAuthenticationProvider daoAuthenticationProvider, VisitorService visitorService) {
        this.visitorService = visitorService;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @GetMapping
    public ModelAndView signUp(){
        return new ModelAndView("sign_up");
    }

    @PostMapping
    public String newUser(@ModelAttribute VisitorDto visitorDto){
        boolean isExist = visitorService.findByEmail(visitorDto.getEmail()).isPresent();
        boolean isValid = VisitorValidator.isValid(visitorDto);

        if(!isExist && isValid) {
            LOGGER.info("New user {}", visitorService.save(VisitorMapper.toUser(visitorDto)));
        }
        return visitorDto + " ,ex: " + isExist + " ,val: " + isValid;
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        return new ModelAndView("login");
    }
}
