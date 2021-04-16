package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.webapp.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    //
    @ExceptionHandler(value = {UserNotFoundException.class, RestaurantNotFoundException.class, NoHandlerFoundException.class, MethodArgumentTypeMismatchException.class})
    public ModelAndView handleException(Exception e)
    {
        return new ModelAndView("error" );
    }


}
