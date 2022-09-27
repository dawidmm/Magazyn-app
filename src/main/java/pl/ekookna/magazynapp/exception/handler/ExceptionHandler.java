package pl.ekookna.magazynapp.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.ekookna.magazynapp.warehouse.exception.AmountTooLargeException;
import pl.ekookna.magazynapp.warehouse.exception.ArticleExistException;
import pl.ekookna.magazynapp.warehouse.exception.ArticleStockNotFoundException;
import pl.ekookna.magazynapp.warehouse.exception.WarehouseNotFoundException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandler {

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(BindException.class)
    public ResponseEntity<String> wrongDataBinding(BindException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(ArticleStockNotFoundException.class)
    public ResponseEntity<String> articleStockNotFound(ArticleStockNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(WarehouseNotFoundException.class)
    public ResponseEntity<String> warehouseNotFound(WarehouseNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(AmountTooLargeException.class)
    public ResponseEntity<String> amountTooLarge(AmountTooLargeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(ArticleExistException.class)
    public ResponseEntity<String> articleExist(ArticleExistException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> constraintViolation(ConstraintViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
