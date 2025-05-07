package ru.freeomsk.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.freeomsk.subscription.entity.User;

/**
 * Репозиторий для управления сущностями {@link User}.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}