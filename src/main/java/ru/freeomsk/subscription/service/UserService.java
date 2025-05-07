package ru.freeomsk.subscription.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.freeomsk.subscription.dto.UserDTO;
import ru.freeomsk.subscription.exception.UserNotFoundException;
import ru.freeomsk.subscription.entity.User;
import ru.freeomsk.subscription.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для управления пользователями.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    /**
     * Конструктор для создания нового экземпляра UserService с заданным репозиторием.
     *
     * @param userRepository репозиторий пользователей.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Создает нового пользователя.
     *
     * @param userDTO данные пользователя.
     * @return созданный пользователь.
     * @throws DataAccessException если произошла ошибка при доступе к данным.
     */
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Создание пользователя: {}", userDTO);
        try {
            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            User createdUser = userRepository.save(user);
            userDTO.setId(createdUser.getId());
            return userDTO;
        } catch (DataAccessException e) {
            logger.error("Ошибка при создании пользователя: {}", userDTO, e);
            throw new RuntimeException("Ошибка при создании пользователя", e);
        }
    }

    /**
     * Получает пользователя по ID.
     *
     * @param id ID пользователя.
     * @return Optional с данными пользователя, если он найден.
     * @throws DataAccessException если произошла ошибка при доступе к данным.
     */
    public Optional<UserDTO> getUserById(Long id) {
        logger.info("Получение пользователя с ID: {}", id);
        try {
            return userRepository.findById(id).map(user -> {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setName(user.getName());
                userDTO.setEmail(user.getEmail());
                return userDTO;
            });
        } catch (DataAccessException e) {
            logger.error("Ошибка при получении пользователя с ID: {}", id, e);
            throw new RuntimeException("Ошибка при получении пользователя", e);
        }
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param id ID пользователя.
     * @param userDTO новые данные пользователя.
     * @return обновленный пользователь.
     * @throws UserNotFoundException если пользователь с указанным ID не найден.
     * @throws DataAccessException если произошла ошибка при доступе к данным.
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        logger.info("Обновление пользователя с ID: {}", id);
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            User updatedUser = userRepository.save(user);
            userDTO.setId(updatedUser.getId());
            return userDTO;
        } catch (UserNotFoundException e) {
            logger.error("Пользователь с ID: {} не найден", id, e);
            throw e;
        } catch (DataAccessException e) {
            logger.error("Ошибка при обновлении пользователя с ID: {}", id, e);
            throw new RuntimeException("Ошибка при обновлении пользователя", e);
        }
    }

    /**
     * Удаляет пользователя по ID.
     *
     * @param id ID пользователя.
     * @throws UserNotFoundException если пользователь с указанным ID не найден.
     * @throws DataAccessException если произошла ошибка при доступе к данным.
     */
    public void deleteUser(Long id) {
        logger.info("Удаление пользователя с ID: {}", id);
        try {
            if (!userRepository.existsById(id)) {
                throw new UserNotFoundException(id);
            }
            userRepository.deleteById(id);
        } catch (UserNotFoundException e) {
            logger.error("Пользователь с ID: {} не найден", id, e);
            throw e;
        } catch (DataAccessException e) {
            logger.error("Ошибка при удалении пользователя с ID: {}", id, e);
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }

    /**
     * Получает список всех пользователей.
     *
     * @return список всех пользователей.
     * @throws DataAccessException если произошла ошибка при доступе к данным.
     */
    public List<UserDTO> getAllUsers() {
        logger.info("Получение всех пользователей");
        try {
            return userRepository.findAll().stream().map(user -> {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setName(user.getName());
                userDTO.setEmail(user.getEmail());
                return userDTO;
            }).collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Ошибка при получении всех пользователей", e);
            throw new RuntimeException("Ошибка при получении всех пользователей", e);
        }
    }
}