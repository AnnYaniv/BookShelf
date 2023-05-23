package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.VisitorDto;
import com.yaniv.bookshelf.mapper.VisitorMapper;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.security.UserDetailsServiceImpl;
import com.yaniv.bookshelf.service.VisitorService;
import com.yaniv.bookshelf.service.VisitorValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger("controller-log");
    private final VisitorService visitorService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public AuthController(VisitorService visitorService, UserDetailsServiceImpl userDetailsService) {
        this.visitorService = visitorService;
        this.userDetailsService = userDetailsService;
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
            Visitor current = visitorService.save(VisitorMapper.toUser(visitorDto));
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(current.getEmail());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, current.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } catch (Exception e) {
                LOGGER.info("Exception: {}", e.getMessage());
            }
        }
        return new RedirectView("/user");
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        return new ModelAndView("login");
    }
}
