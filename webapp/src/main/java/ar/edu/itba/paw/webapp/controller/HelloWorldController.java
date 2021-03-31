package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.User;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.forms.UserForm;

@Controller
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("home");

        return mav;
    }

    @RequestMapping(path ={  "/register" }, method = RequestMethod.GET)
    public ModelAndView registerForm(@ModelAttribute("userForm") final UserForm form) {
        return new ModelAndView("register");
    }

    @RequestMapping(path ={  "/register" }, method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors ) {
        // if there are errors it goes back to the register form without losing data
        // but letting the user know it has errors
        if (errors.hasErrors()) {
            return registerForm(form);
        }

        final User user = userService.register(form.getUsername());
        return new ModelAndView("redirect:/user/" + user.getId());
    }

    @RequestMapping("/user/{userId}")
    public ModelAndView user(@PathVariable("userId") final long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }

}
