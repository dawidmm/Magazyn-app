package pl.ekookna.magazynapp.warehouse.exception;

public class ArticleExistException extends RuntimeException {

    public ArticleExistException(String message) {
        super(message);
    }
}
