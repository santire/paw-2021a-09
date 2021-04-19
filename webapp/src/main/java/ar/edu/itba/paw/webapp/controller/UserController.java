package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import ar.edu.itba.paw.service.RestaurantService;

import ar.edu.itba.paw.webapp.forms.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;

    @RequestMapping("/user/{userId}")
    public ModelAndView user(@PathVariable("userId") final long id) {
        final ModelAndView mav = new ModelAndView("home");
        mav.addObject("user", userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }



    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.GET)
    public ModelAndView editUser(@ModelAttribute("updateUserForm") final UserForm form) {

        final ModelAndView mav = new ModelAndView("editUser");
        User user = loggedUser().orElseThrow(UserNotFoundException::new);

        mav.addObject("user", user);
        List<Restaurant> restaurants = restaurantService.getRestaurantsFromOwner(user.getId());
        mav.addObject("restaurants", restaurants);

        return mav;
    }

    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-username")
    public ModelAndView editUserUsername(@Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {
        if (errors.hasErrors()) {
            return editUser(form);
        }
            User user = loggedUser().orElseThrow(UserNotFoundException::new);
            userService.updateUsername(user.getId(), form.getUsername());
            return new ModelAndView("redirect:/user/edit");
    }

    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-password")
    public ModelAndView editUserPassword(@Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {
        if (errors.hasErrors()) {
            return editUser(form);
        }
        User user = loggedUser().orElseThrow(UserNotFoundException::new);
        userService.updatePassword(user.getId(), form.getPassword());
        return new ModelAndView("redirect:/user/edit");
    }

    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-first-name")
    public ModelAndView editUserFirstName(@Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {
        if (errors.hasErrors()) {
            return editUser(form);
        }
        User user = loggedUser().orElseThrow(UserNotFoundException::new);
        userService.updateFistName(user.getId(), form.getFirst_name());
        return new ModelAndView("redirect:/user/edit");
    }


    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-last-name")
    public ModelAndView editUserLastName(@Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {

        if (errors.hasErrors()) {
            return editUser(form);
        }
        User user = loggedUser().orElseThrow(UserNotFoundException::new);
        userService.updateLastName(user.getId(), form.getLast_name());
        return new ModelAndView("redirect:/user/edit");
    }

    /*
    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-email")
    public ModelAndView editUserEmail(@Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {

        if (errors.hasErrors()) {
            return editUser(form);
        }
        try{
            User user = loggedUser().orElseThrow(UserNotFoundException::new);
            userService.updateEmail(user.getId(), form.getEmail());
            return new ModelAndView("redirect:/logout");
        } catch (Exception e) {
            errors.addError(new ObjectError("emailError", "email already in use"));
            return userEdit(form);
        }
    }
     */

    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-phone")
    public ModelAndView editUserPhone(@Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {

        if (errors.hasErrors()) {
            return editUser(form);
        }
        User user = loggedUser().orElseThrow(UserNotFoundException::new);
        userService.updatePhone(user.getId(), form.getPhone());
        return new ModelAndView("redirect:/user/edit");
    }





    @ModelAttribute
    public Optional<User> loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = userService.findByEmail((String) auth.getName());
        LOGGER.debug("Logged user is {}", user);
        return user;
    }
}
