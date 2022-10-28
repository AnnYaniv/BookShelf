package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.AuthorDto;
import com.yaniv.bookshelf.mapper.AuthorMapper;
import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.service.AuthorService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);
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
        model.addObject("page", 0);
        model.addObject("authors", authorService.getAll(0));
        return model;
    }

    @GetMapping("/pageable")
    @PreAuthorize("hasAuthority('author:read')")
    public ModelAndView pageableAuthors(@RequestParam int page, @RequestParam String firstName){
        ModelAndView model = new ModelAndView("fragment/author_table");
        LOGGER.info("Page: {}, FirstName: {}",page,firstName);
        Page<Author> pageAuthors;
        if(StringUtils.isBlank(firstName)){
            LOGGER.info("find all");
            pageAuthors = authorService.getAll(page);
        } else {
            LOGGER.info("find by author");
            pageAuthors = authorService.getByName(firstName, page);
        }
        if((pageAuthors.getTotalPages() - 1 < page)&&(pageAuthors.getTotalPages()!=0)){
            page = pageAuthors.getTotalPages() - 1;
            if(StringUtils.isBlank(firstName)){
                pageAuthors = authorService.getAll(page);
            } else {
                pageAuthors = authorService.getByName(firstName, page);
            }
        }
        model.addObject("page", page);
        model.addObject("authors", pageAuthors.getContent());
        return model;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('author:write')")
    public ModelAndView createAuthor(@ModelAttribute AuthorDto authorDto){
        authorService.save(AuthorMapper.toAuthor(authorDto));
        return editAuthors();
    }
}
