package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.VisitorDto;
import com.yaniv.bookshelf.mapper.VisitorMapper;
import com.yaniv.bookshelf.service.VisitorService;
import com.yaniv.bookshelf.service.VisitorValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger("controller-log");
    private final VisitorService visitorService;

    @Autowired
    public AuthController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping
    public ModelAndView signUp(){
        return new ModelAndView("sign_up");
    }

    @PostMapping
    public RedirectView newUser(@ModelAttribute VisitorDto visitorDto){
        LOGGER.info("Dto: {}",visitorDto);
        boolean isExist = visitorService.findByEmail(visitorDto.getEmail()).isPresent();
        boolean isValid = VisitorValidator.isValid(visitorDto);

        if(!isExist && isValid) {
            LOGGER.info("New user {}", visitorService.save(VisitorMapper.toUser(visitorDto)));
        }
        return new RedirectView("/user");
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        return new ModelAndView("login");
    }
}
