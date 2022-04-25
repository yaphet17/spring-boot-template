package com.yaphet.springreacttemplate.controllers.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    private final Logger logger= LoggerFactory.getLogger(CustomErrorController.class);
    @RequestMapping("error")
    public String handleError(HttpServletRequest request){
        Object status=request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status!=null){
            Integer statusCode=Integer.valueOf(status.toString());
            if(statusCode==HttpStatus.NOT_FOUND.value()){
                logger.warn("page not found");
                return "error-404";
            }else if(statusCode==HttpStatus.INTERNAL_SERVER_ERROR.value()){
                logger.warn("internal server error");
                return "error-500";
            }
        }
        logger.warn("unknown error occurred error_code "+status);
        return "error";
    }
}
