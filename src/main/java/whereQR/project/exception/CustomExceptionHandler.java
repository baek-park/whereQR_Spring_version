package whereQR.project.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

@RestControllerAdvice
public class CustomExceptionHandler{

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity handleDataException(CustomException customException) {
        ErrorResponse errorResponse = new ErrorResponse(customException.getErrorType(), customException.getMessage(), customException.getPath());
        return ResponseEntity.builder()
                .status(Status.FAILED)
                .data(errorResponse)
                .build();
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity handleDataException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorType.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.builder()
                .status(Status.FAILED)
                .data(errorResponse)
                .build();
    }

}
