package ru.freeomsk.subscription.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.freeomsk.subscription.dto.SubscriptionDTO;
import ru.freeomsk.subscription.service.SubscriptionService;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST-контроллер для управления подписками.
 */
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);
    private final SubscriptionService subscriptionService;

    /**
     * Конструктор для создания нового экземпляра SubscriptionController с заданным сервисом.
     *
     * @param subscriptionService сервис подписок.
     */
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * POST /users/{userId} : Добавить новую подписку для пользователя.
     *
     * @param userId ID пользователя.
     * @param subscriptionDTO данные подписки.
     * @return созданная подписка.
     */
    @PostMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDTO addSubscription(@PathVariable Long userId, @Valid @RequestBody SubscriptionDTO subscriptionDTO) {
        logger.info("Получен запрос на добавление подписки для пользователя с ID: {}", userId);
        return subscriptionService.addSubscription(userId, subscriptionDTO);
    }

    /**
     * GET /users/{userId} : Получить все подписки пользователя.
     *
     * @param userId ID пользователя.
     * @return список подписок пользователя.
     */
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<SubscriptionDTO> getUserSubscriptions(@PathVariable Long userId) {
        logger.info("Получен запрос на получение подписок для пользователя с ID: {}", userId);
        return subscriptionService.getUserSubscriptions(userId);
    }

    /**
     * DELETE /{subscriptionId}/users/{userId} : Удалить подписку пользователя.
     *
     * @param userId ID пользователя.
     * @param subscriptionId ID подписки.
     */
    @DeleteMapping("{subscriptionId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(@PathVariable Long userId, @PathVariable Long subscriptionId) {
        logger.info("Получен запрос на удаление подписки с ID: {} для пользователя с ID: {}", subscriptionId, userId);
        subscriptionService.deleteSubscription(userId, subscriptionId);
    }

    /**
     * GET /top : Получить ТОП-3 подписок по количеству.
     *
     * @return список названий сервисов топовых подписок.
     */
    @GetMapping("/top")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getTopSubscriptions() {
        logger.info("Получен запрос на получение популярных подписок");
        return subscriptionService.getTopSubscriptions();
    }
}