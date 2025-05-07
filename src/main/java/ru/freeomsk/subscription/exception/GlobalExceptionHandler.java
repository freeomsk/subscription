package ru.freeomsk.subscription.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Глобальный обработчик исключений для обработки исключений, возникающих в приложении.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение UserNotFoundException.
     *
     * @param ex исключение UserNotFoundException.
     * @return ответ с HTTP статусом 404 и сообщением об ошибке.
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Обрабатывает исключение SubscriptionNotFoundException.
     *
     * @param ex исключение SubscriptionNotFoundException.
     * @return ответ с HTTP статусом 404 и сообщением об ошибке.
     */
    @ExceptionHandler(SubscriptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleSubscriptionNotFoundException(SubscriptionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Обрабатывает исключение SubscriptionNotBelongToUserException.
     *
     * @param ex исключение SubscriptionNotBelongToUserException.
     * @return ответ с HTTP статусом 400 и сообщением об ошибке.
     */
    @ExceptionHandler(SubscriptionNotBelongToUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleSubscriptionNotBelongToUserException(SubscriptionNotBelongToUserException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Обрабатывает все остальные исключения.
     *
     * @param ex общее исключение.
     * @return ответ с HTTP статусом 500 и сообщением об ошибке.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла непредвиденная ошибка: " + ex.getMessage());
    }
}
