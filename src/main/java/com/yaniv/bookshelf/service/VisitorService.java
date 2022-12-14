package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.Role;
import com.yaniv.bookshelf.repository.VisitorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger("service-log");

    @Autowired
    public VisitorService(VisitorRepository visitorRepository){
        this.visitorRepository = visitorRepository;
    }

    public Visitor save(Visitor visitor){
        LOGGER.info("New user: {}", visitor);
        return visitorRepository.save(visitor);
    }

    public Iterable<Visitor> getAll() {
        return visitorRepository.findAll();
    }

    public Optional<Visitor> findById(String id){
        return visitorRepository.findById(id);
    }
    public Optional<Visitor> findByEmail(String email){
        return visitorRepository.findByEmail(email);
    }
    public Visitor createUser(Visitor visitor){
        LOGGER.info("new user creating {}", visitor.getEmail());
        visitor.setRole(Role.USER);
        return visitorRepository.save(visitor);
    }
}
