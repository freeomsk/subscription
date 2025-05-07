# Тестовое задание в компанию ВебРайз

Необходимо разработать микросервис на Spring Boot 3, который будет
предоставлять REST API для управления пользователями и их подписками на
сервисы.

Проект должен использовать Java 17.

## Функциональные требования

1. API для управления пользователями
- Создание пользователя.
- Получение информации о пользователе.
- Обновление данных пользователя.
- Удаление пользователя.
2. API для подписок
- Добавление подписки пользователю.
- Получение списка подписок пользователя.
- Удаление подписки.
- Подписки представляют собой подписки на цифровые сервисы, такие как
  YouTube Premium, VK Музыка, Яндекс.Плюс, Netflix и другие стриминговые
  платформы.
3. Интеграция с базой данных
- Использовать PostgreSQL.
- Таблицы: users, subscriptions.
4. Логирование
- Логирование через SLF4J.
5. Docker
- Создать Dockerfile для развертывания сервиса.
- Разработать docker-compose.yml, который позволит локально запускать проект
  вместе с базой данных.

_Примечание:_
Для недопущения дублирования наименований сервисов подписок произведена нормализация БД - добавлена таблица _services_

## Требования к API

_Эндпоинты:_

* POST /users - создать пользователя
* GET /users/{id} - получить информацию о пользователе
* PUT /users/{id} - обновить пользователя
* DELETE /users/{id} - удалить пользователя
* POST /subscriptions/users/{id} - добавить подписку
* GET /subscriptions/users/{id} - получить подписки пользователя
* DELETE /subscriptions/{sub_id}/users/{id} - удалить подписку
* GET /subscriptions/top - получить ТОП-3 популярных подписок

## Примеры запросов
* Создать 1 пользователя

`curl -X POST http://localhost:8081/users -H "Content-Type: application/json" -d '{"name": "Ivan Petrov", "email": "ivan.petrov@example.com"}'`

* Создать 2 пользователя

`curl -X POST http://localhost:8081/users -H "Content-Type: application/json" -d '{"name": "Irina Sorokina", "email": "irina.sorokina@example.com"}'`

* Получить пользователя с id = 1

`curl -X GET http://localhost:8081/users/1`

* Получить пользователя с id = 2

`curl -X GET http://localhost:8081/users/2`

* Получить ошибку, если пользователь c id = 3 не найден

`curl -X GET http://localhost:8081/users/3`

* Изменить данные пользователя с id = 1

`curl -X PUT http://localhost:8081/users/1 -H "Content-Type: application/json" -d '{"name": "Petr Ivanov", "email": "petr.ivanov@example.com"}'`

* Создать подписки для пользователя с id = 1

`curl -X POST http://localhost:8081/subscriptions/users/1 -H "Content-Type: application/json" -d '{"nameService": "Netflix"}'`

`curl -X POST http://localhost:8081/subscriptions/users/1 -H "Content-Type: application/json" -d '{"nameService": "YouTube Premium"}'`

`curl -X POST http://localhost:8081/subscriptions/users/1 -H "Content-Type: application/json" -d '{"nameService": "VK Музыка"}'`

* Создать подписки для пользователя с id = 2

`curl -X POST http://localhost:8081/subscriptions/users/2 -H "Content-Type: application/json" -d '{"nameService": "Netflix"}'`

`curl -X POST http://localhost:8081/subscriptions/users/2 -H "Content-Type: application/json" -d '{"nameService": "YouTube Premium"}'`

`curl -X POST http://localhost:8081/subscriptions/users/2 -H "Content-Type: application/json" -d '{"nameService": "VK Музыка"}'`

`curl -X POST http://localhost:8081/subscriptions/users/2 -H "Content-Type: application/json" -d '{"nameService": "Яндекс.Плюс"}'`

* Получить подписки пользователя с id = 1

`curl -X GET http://localhost:8081/subscriptions/users/1`

* Получить ТОП-3 подписок

`curl -X GET http://localhost:8081/subscriptions/top`

* Удалить подписку с id = 2 у пользователя с id = 1

`curl -X DELETE http://localhost:8081/subscriptions/2/users/1`

* Удалить пользователя с id = 1

`curl -X DELETE http://localhost:8081/users/1`