package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.forms.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.edu.itba.paw.service.UserService;

import javax.validation.Valid;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(path = { "/user/edit" }, method = RequestMethod.GET)
    public ModelAndView editUser(
            @ModelAttribute("loggedUser") final User loggedUser,
            @ModelAttribute("updateUserForm") final UserForm form) {

        final ModelAndView mav = new ModelAndView("editUser");
        return mav;
    }

    @RequestMapping(path = { "/user/edit" }, method = RequestMethod.POST)
    public ModelAndView editUser(
            @ModelAttribute("loggedUser") final User loggedUser,
            @Valid @ModelAttribute("updateUserForm") final UserForm form,
            final BindingResult errors,
            RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            LOGGER.debug("Form has errors at /user/edit for user {}", loggedUser.getId());
            return editUser(loggedUser, form);
        }
        try {
        userService.updateUser(loggedUser.getId(),
                form.getUsername(),
                loggedUser.getPassword(),
                form.getFirstName(),
                form.getLastName(),
                loggedUser.getEmail(),
                form.getPhone());

        } catch (Exception e){
            redirectAttributes.addFlashAttribute("somethingWrong", true);
            return new ModelAndView("redirect:/user/edit");
        }

        redirectAttributes.addFlashAttribute("editedUser", true);
        return new ModelAndView("redirect:/");
    }

}
