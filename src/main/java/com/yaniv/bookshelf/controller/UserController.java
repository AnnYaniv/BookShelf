package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final VisitorService visitorService;


    @Autowired
    public UserController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @PostMapping
    public Visitor createUser(@RequestBody Visitor visitor){
        LOGGER.info("data - {}",visitor);
        return visitorService.createUser(visitor);
    }

    @GetMapping
    public Iterable<Visitor> getAll() {
        return visitorService.getAll();
    }
}
