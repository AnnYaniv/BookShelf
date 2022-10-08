package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.Role;

public class UserBuilder {
    Visitor visitor;

    public UserBuilder(){
        visitor = new Visitor();
        visitor.setRole(Role.USER);
    }

    public UserBuilder setEmail(String email){
        visitor.setEmail(email);
        return this;
    }

    public UserBuilder setPassword(String password){
        visitor.setPassword(password);
        return this;
    }

    public UserBuilder setUserName(String userName){
        visitor.setUserName(userName);
        return this;
    }

    public Visitor build(){
        return visitor;
    }
}
