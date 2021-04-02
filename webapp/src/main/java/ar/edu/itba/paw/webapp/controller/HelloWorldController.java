package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import javax.validation.Valid;

import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.webapp.forms.SearchForm;
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

import java.util.Optional;

@Controller
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");

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

        final User user = userService.register(form.getUsername(),form.getPassword(),form.getFirst_name(),form.getLast_name(),form.getEmail(),form.getPhone());
        return new ModelAndView("redirect:/user/" + user.getId());
    }

    @RequestMapping("/user/{userId}")
    public ModelAndView user(@PathVariable("userId") final long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping("/search")
    public ModelAndView searchResults(@RequestParam(name = "find") String search){
        final ModelAndView mav = new ModelAndView("search");
        Optional<Restaurant> restaurant = restaurantService.findByName(search);
        if(restaurant.isPresent()){
            mav.addObject("restaurant", restaurant.get().getName());
        }
        else{
            mav.addObject("restaurant", "Nothing found.");
        }
        return mav;
    }

   /* @RequestMapping("/search")
    public ModelAndView searchResults(@Valid @ModelAttribute("SearchForm") final SearchForm form, final BindingResult errors){
        if(errors.hasErrors()){
            return helloWorld();
        }

        final ModelAndView mav = new ModelAndView("search");
        mav.addObject("find", form.getSearch());
        return mav;
    }*/

    @RequestMapping("/landing")
    public ModelAndView landing(){
        final ModelAndView mav = new ModelAndView("landing");
        return mav;
    }



}
