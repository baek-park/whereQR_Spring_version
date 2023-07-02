package whereQR.project.exception.CustomExceptions;

import whereQR.project.exception.CustomException;
import whereQR.project.exception.ErrorType;

public class UnsupportedJwtException extends CustomException {
    public UnsupportedJwtException(String message, String path) {
        super(message, ErrorType.INVALID_TOKEN, path);
    }
}
