package com.poliqlo.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView handleResponseStatusException(ResponseStatusException ex) {
        ModelAndView modelAndView = new ModelAndView();
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        modelAndView.setStatus(status);
        switch (status) {
            case NOT_FOUND:
                modelAndView.setViewName("errorPage/404");
                break;
            case FORBIDDEN:
                modelAndView.setViewName("errorPage/403");
                break;
            case UNAUTHORIZED:
                modelAndView.setViewName("errorPage/401");
                break;
            case BAD_REQUEST:
                modelAndView.setViewName("errorPage/400");
                break;
            case INTERNAL_SERVER_ERROR:
                modelAndView.setViewName("errorPage/500");
                break;
            default:
                modelAndView.setViewName("errorPage/500");
                break;
        }
        return modelAndView;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ModelAndView modelAndView = new ModelAndView("errorPage/400");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }
}
