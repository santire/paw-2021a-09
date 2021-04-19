package ar.edu.itba.paw.webapp.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;

@ControllerAdvice
public class CommonAttributes {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonAttributes.class);

    @Autowired
    private UserService userService;

    @ModelAttribute("loggedUser")
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> maybeUser = userService.findByEmail((String) auth.getName());

        User user = maybeUser.orElse(null);
        LOGGER.debug("Logged user is {}", user);

        return user;

    }

}
