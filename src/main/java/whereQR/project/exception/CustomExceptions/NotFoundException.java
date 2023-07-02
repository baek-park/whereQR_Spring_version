package whereQR.project.exception.CustomExceptions;

import whereQR.project.exception.CustomException;
import whereQR.project.exception.ErrorType;

public class NotFoundException extends CustomException {
    public NotFoundException(String message, String path) {
        super(message, ErrorType.NOT_FOUND, path);
    }
}
