package Project.SWP391_2024_Fall_SE1866_Group1.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse <T> {
    private int code = 1000;
    private String message;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}