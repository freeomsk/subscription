package ru.freeomsk.subscription.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Представляет сущность сервиса в системе подписок.
 * Этот класс сопоставлен с таблицей "services" в базе данных.
 * Каждый сервис имеет уникальный идентификатор и имя.
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "services")
public class NameService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "service_name", unique = true, nullable = false)
    private String serviceName;


}