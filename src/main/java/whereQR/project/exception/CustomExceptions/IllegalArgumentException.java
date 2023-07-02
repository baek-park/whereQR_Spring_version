package whereQR.project.exception.CustomExceptions;

import whereQR.project.exception.CustomException;
import whereQR.project.exception.ErrorType;

public class IllegalArgumentException extends CustomException {
    public IllegalArgumentException(String message, String path) {
        super(message, ErrorType.INVALID_TOKEN, path);
    }
}
