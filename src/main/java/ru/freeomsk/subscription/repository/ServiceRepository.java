package ru.freeomsk.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.freeomsk.subscription.entity.NameService;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностями сервиса в системе подписок.
 * Этот интерфейс наследует функциональность от JpaRepository,
 * предоставляя методы для выполнения операций CRUD и запросов к базе данных.
 */
@Repository
public interface ServiceRepository extends JpaRepository<NameService, Long> {
    /**
     * Находит сервис по его имени.
     *
     * @param serviceName имя сервиса, по которому нужно выполнить поиск.
     * @return объект Optional, содержащий найденный сервис, если он существует, или пустой объект, если сервис не найден.
     */
    Optional<NameService> findByServiceName(String serviceName);
}