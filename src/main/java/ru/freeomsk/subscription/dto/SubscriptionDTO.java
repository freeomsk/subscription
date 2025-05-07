package ru.freeomsk.subscription.dto;

/**
 * DTO (Data Transfer Object) для передачи данных о подписке.
 */
public class SubscriptionDTO {
    private Long id;
    private String serviceName;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}