package whereQR.project.exception.CustomExceptions;

import whereQR.project.exception.CustomException;
import whereQR.project.exception.ErrorType;

public class MalformedJwtException extends CustomException {

    public MalformedJwtException(String message, String path) {
        super(message, ErrorType.INVALID_TOKEN, path);
    }
}
