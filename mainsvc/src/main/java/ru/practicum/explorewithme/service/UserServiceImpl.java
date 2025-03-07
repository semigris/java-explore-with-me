package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.UserMapper;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.base.UserService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getUserById(Long userId) {
        log.debug("Получение пользователя по id: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: {}" + userId + " не найден"));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        log.debug("Получение пользователей с параметрами: ids={}, from={}, size={}", ids, from, size);

        Pageable pageable = PageRequest.of(from / size, size);

        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageable)
                    .stream()
                    .map(userMapper::toUserDto)
                    .toList();
        } else {
            return userRepository.findAllById(ids)
                    .stream()
                    .map(userMapper::toUserDto)
                    .toList();
        }
    }

    @Override
    @Transactional
    public UserDto registerUser(NewUserRequest newUserRequest) {
        log.debug("Регистрация нового пользователя: {}", newUserRequest);
        if (userRepository.existsByEmail(newUserRequest.getEmail())) {
            throw new ConflictException("Такой адрес электронной почты уже зарегистрирован");
        }
        User user = userMapper.toUser(newUserRequest);
        User savedUser = userRepository.save(user);

        log.debug("Новый пользователь зарегистрирован: {}", savedUser);
        return userMapper.toUserDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.debug("Удаление пользователя с id: {}", userId);
        userRepository.deleteById(userId);
        log.debug("Пользователь удален");
    }
}
