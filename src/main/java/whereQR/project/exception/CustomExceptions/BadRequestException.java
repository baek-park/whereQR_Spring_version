package whereQR.project.exception.CustomExceptions;

import whereQR.project.exception.CustomException;

import static whereQR.project.exception.ErrorType.BAD_REQUEST;

public class BadRequestException extends CustomException {
    public BadRequestException(String message, String path) {
        super(message, BAD_REQUEST, path);
    }
}
