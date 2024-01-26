package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {

    List<UserDto> getUsers();

    UserDto getUserById(Long userId);

    UserDto createUser(UserDto user);

    UserDto updateUser(UserDto updateUser, Long userId);

    void deleteUser(Long userId);
}
