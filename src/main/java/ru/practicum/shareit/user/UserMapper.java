package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {

    public static UserDto userToDto(User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
                );
    }

    public static User toUser(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        if (userDto.getId() != 0) {
            user.setId(userDto.getId());
        }
        return user;
    }

}
