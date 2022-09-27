package pl.ekookna.magazynapp.warehouse.exception;

public class ArticleStockNotFoundException extends RuntimeException {

    public ArticleStockNotFoundException(String message) {
        super(message);
    }
}
