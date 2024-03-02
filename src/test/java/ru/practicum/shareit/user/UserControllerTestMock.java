package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserControllerTestMock {
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private UserController userController;
    UserDto userDto1 = new UserDto();
    UserDto userDto2 = new UserDto();
    UserDto userDto3 = new UserDto();
    UserDto userDto4 = new UserDto();
    UserDto userDto5 = new UserDto();
    List<UserDto> userDtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userDto1.setName("test");
        userDto1.setEmail("test@mail.ru");
        userDto2.setName("test2");
        userDto3.setName("test");
        userDto3.setEmail("test@mail.ru");
        userDto4.setEmail("test4@mail4.ru");
        userDto5.setId(1);
        userDto5.setEmail("test5@mail5.ru");
        userDto5.setName("test5");
        userDtos.add(userDto5);
    }

    @Test
    void createUser() throws Exception {
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(userDto1);
        UserDto userDtoCreate = userController.createUser(userDto1);
        assertThat(userDtoCreate.getId(), equalTo(userDto1.getId()));
        assertThat(userDtoCreate.getName(), equalTo(userDto1.getName()));
        assertThat(userDtoCreate.getEmail(), equalTo(userDto1.getEmail()));
    }

    @Test
    void createUserWithNoEmail() throws Exception {
        Mockito.when(userService.createUser(Mockito.any()))
                .thenThrow(new ValidationException("Поле email пустое"));
        ValidationException throwable = assertThrows(ValidationException.class,
                () -> userController.createUser(userDto2));
        assertThat(throwable.getMessage(), equalTo("Поле email пустое"));
    }

    @Test
    void createUserWithExistEmail() throws Exception {
        Mockito.when(userService.createUser(Mockito.any()))
                .thenThrow(new ValidationException("Такой email уже существует"));
        ValidationException throwable = assertThrows(ValidationException.class,
                () -> userController.createUser(userDto3));
        assertThat(throwable.getMessage(), equalTo("Такой email уже существует"));
    }

    @Test
    void getAllUser() throws Exception {
        Mockito.when(userService.getUsers()).thenReturn(userDtos);
        List<UserDto> getUserDtos = userController.getUsers();
        assertThat(getUserDtos.size(), equalTo(userDtos.size()));
    }

    @Test
    void findUserById() throws Exception {
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(userDto5);
        UserDto userDtoGet = userController.getUserById(1L);
        assertThat(userDtoGet.getId(), equalTo(userDto5.getId()));
    }

    @Test
    void findUserByIdWithWrongId() throws Exception {
        Mockito.when(userService.getUserById(Mockito.anyLong()))
                .thenThrow(new NotFoundException("Пользователь не найден"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> userController.getUserById(1L));
        assertThat(notFoundException.getMessage(), equalTo("Пользователь не найден"));
    }

    @Test
    void updateUser() throws Exception {
        Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyLong())).thenReturn(userDto5);
        UserDto userDtoGet = userController.updateUser(userDto4, 1L);
        assertThat(userDtoGet.getId(), equalTo(userDto5.getId()));
    }

    @Test
    void deleteUser() throws Exception {
        userController.deleteUser(1L);
        Mockito.verify(userService).deleteUser(1L);
    }
}