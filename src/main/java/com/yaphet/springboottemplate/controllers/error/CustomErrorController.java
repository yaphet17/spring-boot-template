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
        model.addAttribute("errorMsg", errorMessage);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                logger.warn("page not found : error code " + statusCode);
                return "error/error-404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                logger.warn("internal server error : error code " + statusCode);
                return "error/error-500";
            }
        }
        logger.warn("unknown error occurred");
        return "error/error";
    }
}
