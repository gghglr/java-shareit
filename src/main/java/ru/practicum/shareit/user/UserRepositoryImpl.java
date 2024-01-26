package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExsist;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {

    private Map<Long, User> users = new HashMap<>();//айди юзера и сам юзер
    private final Map<String, User> userEmailRepository = new HashMap<>();
    Long userId = 1L;

    @Override
    public List<UserDto> getUsers() {
        return users.values().stream().map(x -> UserMapper.userToDto(x)).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        if (users.containsKey(userId)) {
            return UserMapper.userToDto(users.get(userId));
        } else {
            throw new NotFoundException("пользователя не существует");
        }
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userEmailRepository.containsKey(userDto.getEmail())) {
            throw new AlreadyExsist("Пользователь c такой почтой уже существует");
        } else {
            if (userDto.getId() == null) {
                userDto.setId(userId);
            }
            User user = UserMapper.toUser(userDto);
            users.put(userId, user);
            userId++;
            userEmailRepository.put(userDto.getEmail(), user);
        }
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto updateUser, Long userId) {
        validateFoundForUser(userId);
        updateUser.setId(userId);
        if (updateUser.getEmail() != null && updateUser.getName() != null) {
            userEmailRepository.remove(users.get(userId).getEmail());
            users.get(userId).setEmail(updateUser.getEmail());
            users.get(userId).setName(updateUser.getName());
            userEmailRepository.put(users.get(userId).getEmail(), users.get(userId));
        } else if (updateUser.getEmail() != null) {
            validateForExistEmailWithOtherOwner(userId, updateUser);
            userEmailRepository.remove(users.get(userId).getEmail());
            users.get(userId).setEmail(updateUser.getEmail());
            userEmailRepository.put(users.get(userId).getEmail(), users.get(userId));
            updateUser.setName(users.get(userId).getName());
        } else {
            users.get(userId).setName(updateUser.getName());
            userEmailRepository.put(users.get(userId).getEmail(), users.get(userId));
            updateUser.setEmail(users.get(userId).getEmail());
        }
        return updateUser;
    }

    @Override
    public void deleteUser(Long userId) {
        if (users.containsKey(userId)) {
            userEmailRepository.remove(users.get(userId).getEmail());
            users.remove(userId);
        } else {
            throw new NotFoundException("Ошибка удаления пользователя: пользователь не найден");
        }
    }

    private void validateFoundForUser(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void validateForExistEmailWithOtherOwner(long userId, UserDto userDto) {
        if (userEmailRepository.containsKey(userDto.getEmail())) {
            if (userEmailRepository.get(userDto.getEmail()).getId() != userId)
                throw new AlreadyExsist("Пользователь с таким email уже существует");
        }
    }
}
