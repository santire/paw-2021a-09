package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.TokenDoesNotExistException;
import ar.edu.itba.paw.model.exceptions.TokenExpiredException;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.forms.EmailForm;
import ar.edu.itba.paw.webapp.forms.PasswordForm;
import ar.edu.itba.paw.webapp.forms.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private PawUserDetailsService pawUserDetailsService;

    @Autowired
    private CommonAttributes ca;

    // UPDATE USER

    @RequestMapping(path = { "/user/edit" }, method = RequestMethod.GET)
    public ModelAndView editUser(
            @ModelAttribute("updateUserForm") final UserForm form) {

        final ModelAndView mav = new ModelAndView("editUser");
        return mav;
    }

    @RequestMapping(path = { "/user/edit" }, method = RequestMethod.POST)
    public ModelAndView editUser(
            @Valid @ModelAttribute("updateUserForm") final UserForm form,
            final BindingResult errors,
            RedirectAttributes redirectAttributes) {

        User loggedUser = ca.loggedUser();

        if (errors.hasErrors()) {
            LOGGER.debug("Form has errors at /user/edit for user {}", loggedUser.getId());
            return editUser(form);
        }
        try {
            userService.updateUser(
                    loggedUser.getId(),
                    form.getUsername(),
                    loggedUser.getPassword(),
                    form.getFirstName(),
                    form.getLastName(),
                    loggedUser.getEmail(),
                    form.getPhone());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("somethingWrong", true);
            return new ModelAndView("redirect:/user/edit");
        }

        redirectAttributes.addFlashAttribute("editedUser", true);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(path = { "/forgot-password" }, method = RequestMethod.GET)
    public ModelAndView forgotPasswordForm(
            @ModelAttribute("emailForm") final EmailForm form,
            final BindingResult errors) {
        return new ModelAndView("resetPassword");
    }

    @RequestMapping(path = { "/forgot-password" }, method = RequestMethod.POST)
    public ModelAndView forgotPassword(
            @Valid @ModelAttribute("emailForm") final EmailForm form,
            final BindingResult errors) {

        if (errors != null && errors.hasErrors()) {
            return forgotPasswordForm(form, errors);
        }

        try {
            userService.requestPasswordReset(form.getEmail(), ca.getUri());
            return new ModelAndView("requestedResetPassword");
        } catch (TokenCreationException e) {
            LOGGER.error("Could not generate token");
            return forgotPasswordForm(form, errors).addObject("tokenError", true);
        }
    }

    @RequestMapping(path = { "/reset-password" }, method = RequestMethod.GET)
    public ModelAndView updatePasswordForm(
            @RequestParam(name="token", required=true) final String token,
            @ModelAttribute("passwordForm") final PasswordForm form,
            final BindingResult errors) {

        return new ModelAndView("updatePassword");
    }

    @RequestMapping(path = { "/reset-password" }, method = RequestMethod.POST)
    public ModelAndView updatePassword(
            @RequestParam(name="token", required=true) final String token,
            @Valid @ModelAttribute("passwordForm") final PasswordForm form,
            final BindingResult errors) {

        if (errors != null && errors.hasErrors()) {
            return updatePasswordForm(token, form, errors);
        }

        User user;
        try {
            user = userService.updatePasswordByToken(token, form.getPassword());
        } catch (TokenExpiredException e) {
            LOGGER.error("token {} is expired", token);
            return new ModelAndView("redirect:/forgot-password").addObject("expiredToken", true);
        } catch (TokenDoesNotExistException e) {
            LOGGER.warn("token {} does not exist", token);
            return new ModelAndView("requestedResetPassword").addObject("invalidToken", true);
        } catch (Exception e) {
            // Ignore
            // Unexpected error happened, showing register screen with generic error message
            return new ModelAndView("redirect:/login").addObject("tokenError", true);
        }

        UserDetails userDetails = pawUserDetailsService.loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ModelAndView("redirect:/");

    }
}
