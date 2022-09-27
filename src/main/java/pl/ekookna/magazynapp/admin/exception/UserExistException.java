package pl.ekookna.magazynapp.admin.exception;

public class UserExistException extends RuntimeException {
    private String message;

    public UserExistException(String message) {
        super(message);
        this.message = message;
    }
}
