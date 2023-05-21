package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.VisitorDto;
import com.yaniv.bookshelf.mapper.VisitorMapper;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.Role;
import com.yaniv.bookshelf.model.enums.SubscribeTime;
import com.yaniv.bookshelf.repository.VisitorRepository;
import com.yaniv.bookshelf.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserController(VisitorService visitorService) {
        this.visitorService = visitorService;
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
        ModelAndView model = new ModelAndView("user_about");
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
        return visitor.getSubscribeExp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
