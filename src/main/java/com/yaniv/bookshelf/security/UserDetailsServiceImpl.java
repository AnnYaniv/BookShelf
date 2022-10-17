package com.yaniv.bookshelf.security;

import com.yaniv.bookshelf.repository.VisitorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final VisitorRepository visitorRepository;

    @Autowired
    public UserDetailsServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        LOGGER.info("visitor - {}", visitorRepository.findByEmail(email));
        return SecurityUser.fromVisitor(visitorRepository.findByEmail(email).orElseThrow(() -> {
            throw new UsernameNotFoundException("email not found");
        }));
    }
}
