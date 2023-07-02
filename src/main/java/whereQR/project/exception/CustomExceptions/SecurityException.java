package whereQR.project.exception.CustomExceptions;

import whereQR.project.exception.CustomException;
import whereQR.project.exception.ErrorType;

public class SecurityException extends CustomException {
    public SecurityException(String message,  String path) {
        super(message, ErrorType.INVALID_TOKEN, path);
    }
}
