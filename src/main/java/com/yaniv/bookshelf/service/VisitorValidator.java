package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.dto.VisitorDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class VisitorValidator {
    private VisitorValidator(){
    }
    public static boolean isValid(VisitorDto visitor){
        Matcher matcher = Pattern.compile("^(.+)@(\\S+)$").matcher(visitor.getEmail());
        return matcher.matches() && (visitor.getPassword().length() > 8);
    }
}
