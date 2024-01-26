package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepositoryImpl){
        this.userRepository = userRepositoryImpl;
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public UserDto getUserByid(Long userId) {
        return userRepository.getUserByid(userId);
    }

    @Override
    public UserDto createUser(UserDto user) {
        return userRepository.createUser(user);
    }

    @Override
    public UserDto updateUser(UserDto user, Long userId) {
        return userRepository.updateUser(user, userId);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
