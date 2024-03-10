package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers();

    UserDto getUserById(long userId);

    UserDto createUser(UserDto user);

    UserDto updateUser(UserDto user, long userId);

    void deleteUser(long userId);
}
