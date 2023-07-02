package whereQR.project.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final ErrorType errorType;
    private String path;

    public CustomException(String message, ErrorType errorType, String path) {
        super(message);
        this.errorType = errorType;
        this.path = path;
    }

}
