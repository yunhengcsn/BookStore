package bookstore.user.service;

/**
 * @BelongsProject: BookStore
 * @BelongsPackage: bookstore.user.service
 * @Author: csn
 * @Description: UserException
 */
public class UserException extends Exception{
    public UserException() {
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }
}
