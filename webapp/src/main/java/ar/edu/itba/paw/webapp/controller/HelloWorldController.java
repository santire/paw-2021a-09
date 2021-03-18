package ar.edu.itba.paw.webapp.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.service.UserService;

@Controller
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");

        final int usersCount = userService.list().size();
        Random random = new Random();
        final int randomUserId = random.nextInt(usersCount);

        // randomizes user greeted on each response
        mav.addObject("greeting", userService.list().get(randomUserId).getName());
        return mav;
    }
}
