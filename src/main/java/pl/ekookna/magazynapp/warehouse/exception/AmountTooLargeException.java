package pl.ekookna.magazynapp.warehouse.exception;

public class AmountTooLargeException extends RuntimeException {

    public AmountTooLargeException(String message) {
        super(message);
    }
}
