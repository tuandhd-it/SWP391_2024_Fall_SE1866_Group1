package project.dental_clinic_management.exceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    String uncategorizedExceptionHandler(Exception e) {

        return e.getMessage();
    }

}
