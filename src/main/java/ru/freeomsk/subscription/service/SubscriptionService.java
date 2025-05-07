package ru.freeomsk.subscription.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.freeomsk.subscription.dto.SubscriptionDTO;
import ru.freeomsk.subscription.exception.SubscriptionNotBelongToUserException;
import ru.freeomsk.subscription.exception.SubscriptionNotFoundException;
import ru.freeomsk.subscription.exception.UserNotFoundException;
import ru.freeomsk.subscription.entity.Subscription;
import ru.freeomsk.subscription.entity.User;
import ru.freeomsk.subscription.repository.SubscriptionRepository;
import ru.freeomsk.subscription.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления подписками.
 */
@Service
public class SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    /**
     * Конструктор для создания нового экземпляра SubscriptionService с заданными репозиториями.
     *
     * @param subscriptionRepository репозиторий подписок.
     * @param userRepository репозиторий пользователей.
     */
    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Добавляет новую подписку для пользователя.
     *
     * @param userId ID пользователя.
     * @param subscriptionDTO данные подписки.
     * @return созданная подписка.
     * @throws UserNotFoundException если пользователь с указанным ID не найден.
     * @throws DataAccessException если произошла ошибка при доступе к данным.
     */
    public SubscriptionDTO addSubscription(Long userId, SubscriptionDTO subscriptionDTO) {
        logger.info("Добавление подписки для пользователя с ID: {}", userId);
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            Subscription subscription = new Subscription();
            subscription.setServiceName(subscriptionDTO.getServiceName());
            subscription.setUser(user);
            Subscription createdSubscription = subscriptionRepository.save(subscription);
            subscriptionDTO.setId(createdSubscription.getId());
            subscriptionDTO.setUserId(userId);
            return subscriptionDTO;
        } catch (UserNotFoundException e) {
            logger.error("Пользователь с ID: {} не найден", userId, e);
            throw e;
        } catch (DataAccessException e) {
            logger.error("Ошибка при добавлении подписки для пользователя с ID: {}", userId, e);
            throw new RuntimeException("Ошибка при добавлении подписки", e);
        }
    }

    /**
     * Получает все подписки пользователя.
     *
     * @param userId ID пользователя.
     * @return список подписок пользователя.
     * @throws UserNotFoundException если пользователь с указанным ID не найден.
     * @throws DataAccessException если произошла ошибка при доступе к данным.
     */
    public List<SubscriptionDTO> getUserSubscriptions(Long userId) {
        logger.info("Получение подписок для пользователя с ID: {}", userId);
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            return user.getSubscriptions().stream().map(subscription -> {
                SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
                subscriptionDTO.setId(subscription.getId());
                subscriptionDTO.setServiceName(subscription.getServiceName());
                subscriptionDTO.setUserId(subscription.getUser().getId());
                return subscriptionDTO;
            }).collect(Collectors.toList());
        } catch (UserNotFoundException e) {
            logger.error("Пользователь с ID: {} не найден", userId, e);
            throw e;
        } catch (DataAccessException e) {
            logger.error("Ошибка при получении подписок для пользователя с ID: {}", userId, e);
            throw new RuntimeException("Ошибка при получении подписок", e);
        }
    }

    /**
     * Удаляет подписку пользователя.
     *
     * @param userId ID пользователя.
     * @param subscriptionId ID подписки.
     * @throws SubscriptionNotFoundException если подписка с указанным ID не найдена.
     * @throws SubscriptionNotBelongToUserException если подписка не принадлежит пользователю.
     * @throws DataAccessException если произошла ошибка при доступе к данным.
     */
    public void deleteSubscription(Long userId, Long subscriptionId) {
        logger.info("Удаление подписки с ID: {} для пользователя с ID: {}", subscriptionId, userId);
        try {
            Subscription subscription = subscriptionRepository.findById(subscriptionId)
                    .orElseThrow(() -> new SubscriptionNotFoundException("Подписка с ID: " + subscriptionId + " не найдена"));
            if (!subscription.getUser().getId().equals(userId)) {
                throw new SubscriptionNotBelongToUserException(subscriptionId, userId);
            }
            subscriptionRepository.delete(subscription);
        } catch (SubscriptionNotFoundException | SubscriptionNotBelongToUserException e) {
            logger.error("Ошибка при удалении подписки с ID: {} для пользователя с ID: {}", subscriptionId, userId, e);
            throw e;
        } catch (DataAccessException e) {
            logger.error("Ошибка при удалении подписки с ID: {} для пользователя с ID: {}", subscriptionId, userId, e);
            throw new RuntimeException("Ошибка при удалении подписки", e);
        }
    }

    /**
     * Получает топ подписок по количеству.
     *
     * @return список названий сервисов топовых подписок.
     * @throws SubscriptionNotFoundException если подписки не найдены.
     * @throws DataAccessException если произошла ошибка при доступе к данным.
     */
    public List<String> getTopSubscriptions() {
        logger.info("Получение топовых подписок по количеству");
        try {
            Pageable topThree = PageRequest.of(0, 3);
            List<Object[]> results = subscriptionRepository.findTop3Subscriptions(topThree);

            if (results.isEmpty()) {
                throw new SubscriptionNotFoundException("Подписки не найдены");
            }

            return results.stream()
                    .map(result -> (String) result[0]) // Извлечение serviceName
                    .collect(Collectors.toList());
        } catch (SubscriptionNotFoundException e) {
            logger.error("Подписки не найдены", e);
            throw e;
        } catch (DataAccessException e) {
            logger.error("Ошибка при получении топовых подписок", e);
            throw new RuntimeException("Ошибка при получении топовых подписок", e);
        }
    }
}