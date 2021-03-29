package ar.edu.itba.paw.webapp.controller;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionHandlingAdvice  {


    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView ex(NoHandlerFoundException exception) {
        final ModelAndView mav = new ModelAndView("index");

        return mav;
    }





}
