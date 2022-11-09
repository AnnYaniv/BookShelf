package com.yaniv.bookshelf.mapper;

import com.yaniv.bookshelf.dto.VisitorDto;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VisitorMapper {

    private VisitorMapper() {
    }

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

    public static Visitor toUser(VisitorDto visitorDto){
        Visitor user = new Visitor();
        toVisitor(visitorDto, user);
        if(!StringUtils.isBlank(user.getUserName())){
            Matcher matcher = Pattern.compile("^(.+)@(\\S+)$").matcher(user.getEmail());
            if(matcher.find()){
                user.setUserName(matcher.group(0));
            }
        }
        user.setRole(Role.USER);
        return user;
    }
}
