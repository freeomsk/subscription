package ru.freeomsk.subscription.exception;

/**
 * Исключение, выбрасываемое, когда подписка не найдена.
 */
public class SubscriptionNotFoundException extends RuntimeException {

    /**
     * Конструктор для создания нового экземпляра SubscriptionNotFoundException с указанным сообщением.
     *
     * @param message сообщение об ошибке.
     */
    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}