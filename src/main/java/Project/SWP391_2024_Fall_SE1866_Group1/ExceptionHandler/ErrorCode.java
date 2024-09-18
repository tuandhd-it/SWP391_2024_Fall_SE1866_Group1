package Project.SWP391_2024_Fall_SE1866_Group1.ExceptionHandler;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    USER_NOT_FOUND(1001, "User not found"),
    USER_EXISTED(1002, "User already existed"),
    USERNAME_INVALID(1003, "Username is at least 3 characters"),
    PASSWORD_INVALID(1004, "Password is at least 8 characters"),
    INVALID_KEY(1005, "Invalid message key"),
    USER_NOT_EXISTED(1006, "User not existed"),
    UNAUTHORIZED(1007, "Unauthorized"),
    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
