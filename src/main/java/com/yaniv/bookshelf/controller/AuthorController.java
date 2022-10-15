package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.AuthorDto;
import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('author:read')")
    public ModelAndView editAuthors(){
        ModelAndView model = new ModelAndView("author_edit");
        Author author = new Author();
        author.setId(UUID.randomUUID().toString());
        model.addObject("author", author);
        model.addObject("authors", authorService.getAll());
        return model;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('author:write')")
    public String createAuthor(@ModelAttribute AuthorDto authorDto){
        return authorDto.getId() + "<br>" + authorDto.getFirstName() + "<br>" + authorDto.getLastName() + "</br>";
    }
}
