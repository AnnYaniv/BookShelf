package com.yaniv.bookshelf.mapper;

import com.yaniv.bookshelf.dto.AuthorDto;
import com.yaniv.bookshelf.model.Author;

public class AuthorMapper {
    private AuthorMapper() {}
    public static Author toAuthor(AuthorDto authorDto){
        Author author = new Author();
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());
        return author;
    }
}
