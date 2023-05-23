package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.VisitorDto;
import com.yaniv.bookshelf.mapper.VisitorMapper;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.Role;
import com.yaniv.bookshelf.model.enums.SubscribeTime;
import com.yaniv.bookshelf.security.UserDetailsServiceImpl;
import com.yaniv.bookshelf.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.util.EnumUtils;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger("controller-log");
    private final VisitorService visitorService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserController(VisitorService visitorService, UserDetailsServiceImpl userDetailsService) {
        this.visitorService = visitorService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public ModelAndView getInfo() {
        return new ModelAndView("user");
    }

    @GetMapping("/about")
    public ModelAndView getInfoById(Principal principal) {
        Visitor visitor = visitorService.findByEmail(principal.getName()).orElse(new Visitor());
        ModelAndView model = new ModelAndView("fragment/user_about");
        model.addObject("visitor", visitor);
        model.addObject("additionInfo", "");
        return model;
    }

    @PostMapping("/about")
    public ModelAndView updateUser(Principal principal, VisitorDto visitorDto) {
        LOGGER.debug("post /user/about");
        Visitor visitor = visitorService.save(VisitorMapper.toVisitor(visitorDto,
                visitorService.findByEmail(principal.getName()).orElse(new Visitor())));
        ModelAndView model = new ModelAndView("fragment/user_about");
        UserDetails userDetails = userDetailsService.loadUserByUsername(visitor.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, visitor.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        model.addObject("visitor", visitor);
        model.addObject("additionInfo", "Updated successfully");
        return model;
    }

    @PostMapping("/subscribe")
    public String subscribe(Principal principal, @RequestParam String time) {
        Visitor visitor = visitorService.save(visitorService.findByEmail(principal.getName()).orElse(new Visitor()));
        SubscribeTime subscribeTime = EnumUtils.findEnumInsensitiveCase(SubscribeTime.class, time);
        visitor.setSubscribeExp(LocalDate.now().plusMonths(subscribeTime.getCount()));
        visitor.setRole(Role.SUBSCRIBER);
        visitorService.save(visitor);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),
                auth.getCredentials(), visitor.getRole().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return visitor.getSubscribeExp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(Principal principal){
        Visitor visitor = visitorService.findByEmail(principal.getName()).orElse(new Visitor());
        visitor.setSubscribeExp(null);
        visitorService.isSubscribed(visitor.getId());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),
                auth.getCredentials(), visitor.getRole().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return visitor.toString();
    }
}
