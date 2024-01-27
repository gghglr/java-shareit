package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepositoryImpl) {
        this.userRepository = userRepositoryImpl;
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.getUsers().stream().map(x -> UserMapper.userToDto(x)).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.userToDto(userRepository.getUserById(userId));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return UserMapper.userToDto(userRepository.createUser(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(UserDto user, Long userId) {

        return UserMapper.userToDto(userRepository.updateUser(UserMapper.toUser(user), userId));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
