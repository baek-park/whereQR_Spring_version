package whereQR.project.exception.CustomExceptions;

import whereQR.project.exception.CustomException;
import whereQR.project.exception.ErrorType;

public class InvalidUsername extends CustomException {
    public InvalidUsername(String message, String path) {
        super(message, ErrorType.INVALID_USERNAME, path);
    }
}
