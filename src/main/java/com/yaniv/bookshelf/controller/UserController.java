package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.VisitorDto;
import com.yaniv.bookshelf.mapper.VisitorMapper;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger("controller-log");
    private final VisitorService visitorService;

    @Autowired
    public UserController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping
    public ModelAndView getInfo() {
        return new ModelAndView("user");
    }

    @GetMapping("/about")
    public ModelAndView getInfoById(Principal principal) {
        Visitor visitor = visitorService.findByEmail(principal.getName()).orElse(new Visitor());
        ModelAndView model = new ModelAndView("fragment/user_update");
        model.addObject("visitor", visitor);
        model.addObject("additionInfo", "");
        return model;
    }

    @PostMapping("/about")
    public ModelAndView updateUser(Principal principal, VisitorDto visitorDto) {
        LOGGER.debug("post /user/about");
        Visitor visitor = visitorService.save(VisitorMapper.toVisitor(visitorDto,
                visitorService.findByEmail(principal.getName()).orElse(new Visitor())));
        ModelAndView model = new ModelAndView("fragment/user_update");
        model.addObject("visitor", visitor);
        model.addObject("additionInfo", "Updated successfully");
        return model;
    }

}
