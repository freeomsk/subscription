package ru.freeomsk.subscription.exception;

/**
 * Исключение, выбрасываемое, когда подписка не принадлежит пользователю.
 */
public class SubscriptionNotBelongToUserException extends RuntimeException {
    /**
     * Конструктор для создания нового экземпляра SubscriptionNotBelongToUserException с указанными ID подписки и пользователя.
     *
     * @param subscriptionId ID подписки.
     * @param userId ID пользователя.
     */
    public SubscriptionNotBelongToUserException(Long subscriptionId, Long userId) {
        super("Подписка с ID: " + subscriptionId + " не принадлежит пользователю с ID: " + userId);
    }
}
