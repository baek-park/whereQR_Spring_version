package whereQR.project.exception.CustomExceptions;

import whereQR.project.exception.CustomException;
import whereQR.project.exception.ErrorType;

public class InternalException extends CustomException {
    public InternalException(String message, String path) {
        super(message, ErrorType.INTERNAL_SERVER_ERROR, path);
    }
}
