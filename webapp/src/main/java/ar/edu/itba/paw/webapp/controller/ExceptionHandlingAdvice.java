package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.webapp.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;

import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionHandlingAdvice  {

    /*
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView ex(NoHandlerFoundException exception) {
        final ModelAndView mav = new ModelAndView("home");

        return mav;
    }
     */

    @ExceptionHandler(value = { NoHandlerFoundException.class})
    public ModelAndView notFound(Exception e)
    {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", 404);
        return mav;
    }


    //
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ModelAndView methodArgumentTypeMismatch(Exception e)
    {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", 400);
        return mav;
    }

    //
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ModelAndView userNotFound(Exception e)
    {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", 499);
        return mav;
    }

    //
    @ExceptionHandler(value = {RestaurantNotFoundException.class})
    public ModelAndView restaurantNotFound(Exception e)
    {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", 498);
        return mav;
    }

}
