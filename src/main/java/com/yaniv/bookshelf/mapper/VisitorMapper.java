package com.yaniv.bookshelf.mapper;

import com.yaniv.bookshelf.dto.VisitorDto;
import com.yaniv.bookshelf.model.Visitor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class VisitorMapper {

    public static Visitor toVisitor(VisitorDto visitorDto, Visitor visitor){
        if(!StringUtils.isBlank(visitorDto.getEmail())) {
            visitor.setEmail(visitorDto.getEmail());
        }
        if(!StringUtils.isBlank(visitorDto.getUserName())){
            visitor.setUserName(visitorDto.getUserName());
        }
        if(!StringUtils.isBlank(visitorDto.getPassword())){
            visitor.setPassword(new BCryptPasswordEncoder(12).encode(visitorDto.getPassword()));
        }
        return visitor;
    }
}
