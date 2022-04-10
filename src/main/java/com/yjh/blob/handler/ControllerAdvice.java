package com.yjh.blob.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    public final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e)throws  Exception{
        logger.error("request URL:{}",request.getRequestURI());

        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!=null){
            throw  e;
        }
        ModelAndView modelAndView =new ModelAndView();
        modelAndView.addObject("url",request.getRequestURI());
        modelAndView.addObject("exception",e);
        modelAndView.setViewName("error/error");
        return  modelAndView;
    }


}
