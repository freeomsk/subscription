package ru.freeomsk.subscription.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.freeomsk.subscription.entity.Subscription;

import java.util.List;

/**
 * Репозиторий для управления сущностями {@link Subscription}.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Находит топ подписок, сгруппированных по названию сервиса и отсортированных по количеству подписок в порядке убывания.
     *
     * @param pageable информация о пагинации.
     * @return список объектов, где каждый объект содержит название сервиса и количество подписок.
     */
    @Query("SELECT s.nameService.serviceName, COUNT(s) " +
            "FROM Subscription s " +
            "GROUP BY s.nameService.serviceName " +
            "ORDER BY COUNT(s) DESC")
    List<Object[]> findTop3Subscriptions(Pageable pageable);
}