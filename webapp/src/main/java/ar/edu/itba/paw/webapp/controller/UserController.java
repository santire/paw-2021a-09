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
import org.springframework.web.bind.annotation.*;
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
    public ModelAndView editUser(@ModelAttribute("loggedUser") final User loggedUser, @ModelAttribute("updateUserForm") final UserForm form, @RequestParam(value = "error", required = false) final String error) {

        final ModelAndView mav = new ModelAndView("editUser");

        if(error != null){
            mav.addObject("error", error);
        }
        mav.addObject("user", loggedUser);
        List<Restaurant> restaurants = restaurantService.getRestaurantsFromOwner(1,1,loggedUser.getId());
        mav.addObject("restaurants", restaurants);

        return mav;
    }

    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-username")
    public ModelAndView editUserUsername(@ModelAttribute("loggedUser") final User loggedUser, @Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {
        if (errors.hasErrors()) {
            return editUser(loggedUser, form, "");
        }
            userService.updateUsername(loggedUser.getId(), form.getUsername());
            return new ModelAndView("redirect:/user/edit");
    }

    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-password")
    public ModelAndView editUserPassword(@ModelAttribute("loggedUser") final User loggedUser, @Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {
        if(!form.getPassword().equals(form.getRepeatPassword())){
            return editUser(loggedUser, form, "password");
        }
        if (errors.hasErrors()) {
            return editUser(loggedUser, form,null);
        }
        userService.updatePassword(loggedUser.getId(), form.getPassword());
        return new ModelAndView("redirect:/user/edit");
    }

    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-first-name")
    public ModelAndView editUserFirstName(@ModelAttribute("loggedUser") final User loggedUser, @Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {
        if (errors.hasErrors()) {
            return editUser(loggedUser, form, "");
        }
        userService.updateFistName(loggedUser.getId(), form.getFirstName());
        return new ModelAndView("redirect:/user/edit");
    }


    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-last-name")
    public ModelAndView editUserLastName(@ModelAttribute("loggedUser") final User loggedUser, @Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {

        if (errors.hasErrors()) {
            return editUser(loggedUser, form, "");
        }
        userService.updateLastName(loggedUser.getId(), form.getLastName());
        return new ModelAndView("redirect:/user/edit");
    }

    /*
    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-email")
    public ModelAndView editUserEmail(@Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {

        if (errors.hasErrors()) {
            return editUser(form, "");
        }
        try{
            User user = loggedUser().orElseThrow(UserNotFoundException::new);
            userService.updateEmail(user.getId(), form.getEmail());
            return new ModelAndView("redirect:/logout");
        } catch (Exception e) {
            //
            return userEdit(form);
        }
    }
     */

    @RequestMapping(path ={"/user/edit"}, method = RequestMethod.POST, params = "edit-phone")
    public ModelAndView editUserPhone(@ModelAttribute("loggedUser") final User loggedUser, @Valid @ModelAttribute("updateUserForm") final UserForm form, final BindingResult errors ) {

        if (errors.hasErrors()) {
            return editUser(loggedUser, form, "");
        }
        userService.updatePhone(loggedUser.getId(), form.getPhone());
        return new ModelAndView("redirect:/user/edit");
    }




    /*
    @ModelAttribute
    public Optional<User> loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user = userService.findByEmail((String) auth.getName());
        LOGGER.debug("Logged user is {}", user);
        return user;
    }

     */
}
