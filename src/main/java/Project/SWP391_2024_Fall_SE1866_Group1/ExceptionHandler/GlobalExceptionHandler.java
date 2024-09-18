package Project.SWP391_2024_Fall_SE1866_Group1.ExceptionHandler;

import Project.SWP391_2024_Fall_SE1866_Group1.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> uncategoriedExceptionHandler(Exception e) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> appExceptionHandler(AppException e) {

        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {

        ErrorCode errorCode;

        try {
            errorCode = ErrorCode.valueOf(e.getFieldError().getDefaultMessage());
        } catch (IllegalArgumentException iae) {
            errorCode = ErrorCode.INVALID_KEY;
        }


        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());

        return ResponseEntity.badRequest().body(apiResponse);
    }

}
