package ru.freeomsk.subscription.exception;

/**
 * Исключение, выбрасываемое, когда пользователь не найден.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Конструктор для создания нового экземпляра UserNotFoundException с указанным ID пользователя.
     *
     * @param id ID пользователя.
     */
    public UserNotFoundException(Long id) {
        super("Пользователь с ID: " + id + " не найден");
    }
}
