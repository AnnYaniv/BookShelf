package com.yaniv.bookshelf.security;

import com.yaniv.bookshelf.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final VisitorRepository visitorRepository;

    @Autowired
    public UserDetailsServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return visitorRepository.findByEmail(email).map(SecurityUser::fromVisitor).orElse(null);
    }
}
