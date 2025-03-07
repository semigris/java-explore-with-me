package ru.practicum.explorewithme.service.base;

import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Long userId);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto registerUser(NewUserRequest newUserRequest);

    void deleteUser(Long userId);
}

