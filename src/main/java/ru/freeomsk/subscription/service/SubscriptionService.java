package ru.freeomsk.subscription.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.freeomsk.subscription.dto.SubscriptionDTO;
import ru.freeomsk.subscription.entity.NameService;
import ru.freeomsk.subscription.entity.Subscription;
import ru.freeomsk.subscription.entity.User;
import ru.freeomsk.subscription.exception.SubscriptionNotBelongToUserException;
import ru.freeomsk.subscription.exception.SubscriptionNotFoundException;
import ru.freeomsk.subscription.exception.UserNotFoundException;
import ru.freeomsk.subscription.repository.ServiceRepository;
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
    private final ServiceRepository serviceRepository;

    /**
     * Конструктор для создания экземпляра SubscriptionService.
     *
     * @param subscriptionRepository репозиторий для работы с подписками.
     * @param userRepository репозиторий для работы с пользователями.
     * @param serviceRepository репозиторий для работы с сервисами.
     */
    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, ServiceRepository serviceRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
    }

    /**
     * Добавляет новую подписку для указанного пользователя.
     *
     * @param userId ID пользователя, для которого добавляется подписка.
     * @param subscriptionDTO объект, содержащий данные о подписке.
     * @return объект SubscriptionDTO с данными о созданной подписке.
     * @throws UserNotFoundException если пользователь с указанным ID не найден.
     * @throws DataAccessException если произошла ошибка при доступе к данным.
     */
    public SubscriptionDTO addSubscription(Long userId, SubscriptionDTO subscriptionDTO) {
        logger.info("Добавление подписки для пользователя с ID: {}", userId);
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            NameService service = serviceRepository.findByServiceName(subscriptionDTO.getServiceName())
                    .orElseGet(() -> {
                        NameService newService = new NameService();
                        newService.setServiceName(subscriptionDTO.getServiceName());
                        return serviceRepository.save(newService);
                    });

            Subscription subscription = new Subscription();
            subscription.setNameService(service);
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
     * Получает список подписок для указанного пользователя.
     *
     * @param userId ID пользователя, для которого нужно получить подписки.
     * @return список объектов SubscriptionDTO, представляющих подписки пользователя.
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
                subscriptionDTO.setServiceName(subscription.getNameService().getServiceName());
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
     * Удаляет подписку для указанного пользователя.
     *
     * @param userId ID пользователя, для которого удаляется подписка.
     * @param subscriptionId ID подписки, которую нужно удалить.
     * @throws SubscriptionNotFoundException если подписка с указанным ID не найдена.
     * @throws SubscriptionNotBelongToUserException если подписка не принадлежит указанному пользователю.
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
     * Получает популярные подписки по количеству.
     *
     * @return список названий популярных подписок.
     * @throws SubscriptionNotFoundException если подписки не найдены.
     * @throws DataAccessException если произошла ошибка при доступе к данным.
     */
    public List<String> getTopSubscriptions() {
        logger.info("Получение популярных подписок по количеству");
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
            logger.error("Ошибка при получении популярных подписок", e);
            throw new RuntimeException("Ошибка при получении популярных подписок", e);
        }
    }
}