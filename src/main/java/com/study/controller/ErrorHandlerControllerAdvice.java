package com.study.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorHandlerControllerAdvice {

    @ExceptionHandler(Throwable.class)
    public String error(HttpServletRequest request, Model model) {
        Object statusCodeObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(statusCodeObj != null) {
            int statusCode = Integer.parseInt(statusCodeObj.toString());

            if(statusCode == HttpStatus.UNAUTHORIZED.value()) {
                model.addAttribute("message", "로그인 후 이용해 주십시오.");
            }
        }

        return "study/error";
    }

}