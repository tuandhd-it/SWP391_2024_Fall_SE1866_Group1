package project.dental_clinic_management.exceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//Class để handle exception
@ControllerAdvice
public class GlobalExceptionHandler {

    //Handle exception chung
    @ExceptionHandler(value = Exception.class)
    String uncategorizedExceptionHandler(Exception e) {

        return e.getMessage();
    }

}
