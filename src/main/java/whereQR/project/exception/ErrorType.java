package whereQR.project.exception;

/**
 * https://restfulapi.net/http-status-codes 참고
 */
public enum ErrorType {

    /**
     * 4XX
     */

    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,

    /**
     * 5XX
     */

    INTERNAL_SERVER_ERROR,

    /**
     * other
     */
    INVALID_TOKEN,
    INVALID_USERNAME,
    INVALID_PASSWORD

}
