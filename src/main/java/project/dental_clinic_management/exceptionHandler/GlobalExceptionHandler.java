package project.dental_clinic_management.exceptionHandler;

import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.thymeleaf.exceptions.TemplateInputException;

import java.sql.SQLException;

//Class để handle exception
@ControllerAdvice
public class GlobalExceptionHandler {

    //Handle exception chung
    @ExceptionHandler(value = Exception.class)
    String uncategorizedExceptionHandler(Exception e) {

        return e.getMessage();
    }

    @ExceptionHandler(value = TemplateInputException.class)
    String templateInputExceptionHandler(TemplateInputException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = RequestRejectedException.class)
    String requestRejectedExceptionHandler(RequestRejectedException e) {
        return e.getMessage();
    }

}
