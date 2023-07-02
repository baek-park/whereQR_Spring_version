package whereQR.project.exception.CustomExceptions;

import whereQR.project.exception.CustomException;
import whereQR.project.exception.ErrorType;

public class ForbiddenException extends CustomException {
    public ForbiddenException(String message, String path) {
        super(message, ErrorType.FORBIDDEN, path);
    }
}
