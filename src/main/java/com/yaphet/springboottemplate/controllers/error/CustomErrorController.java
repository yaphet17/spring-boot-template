package com.yaphet.springboottemplate.controllers.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    private final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    public static String getBindingErrorMessage() {
        return "Unable to bind";
    }

    @RequestMapping("error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = String.valueOf(request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        int statusCode = Integer.parseInt(status.toString());

        model.addAttribute("errorMsg", errorMessage);
        model.addAttribute("statusCode", statusCode);

        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            logger.debug("message: " + errorMessage + " : status code " + statusCode);
            return "error/error-404";
        } else if (String.valueOf(statusCode).startsWith("4")) {
            logger.debug("message: " + errorMessage + " : status code " + statusCode);
        } else if (String.valueOf(statusCode).startsWith("5")) {
            logger.error("message: internal server error : status code " + statusCode);
        }

        return "error/error";
    }
}
