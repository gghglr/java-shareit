package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTestWithContext {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserServiceImpl userService;

    @Autowired
    private MockMvc mvc;

    private final UserDto userDto1 = new UserDto();
    private final UserDto userDto2 = new UserDto();
    private final UserDto userDto3 = new UserDto();
    private final UserDto userDto4 = new UserDto();
    private final List<UserDto> userDtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userDto1.setEmail("test@mail.ru");
        userDto1.setId(1);
        userDto1.setName("test");
        userDto2.setEmail("update@mail.ru");
        userDto2.setId(1);
        userDto2.setName("update");
        userDto3.setEmail("test2@test2.ru");
        userDto3.setId(2);
        userDto3.setName("test2");
        userDto4.setEmail(null);
        userDto4.setId(1);
        userDto4.setName("noEmail");
        userDtos.add(userDto1);
        userDtos.add(userDto3);
    }


    @Test
    void saveNewUser() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(userDto1);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto1.getId()))
                .andExpect(jsonPath("$.email").value(userDto1.getEmail()))
                .andExpect(jsonPath("$.name").value(userDto1.getName()));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(any(), anyLong())).thenReturn(userDto2);
        mvc.perform(patch("/users/{userId}", 2)
                        .content(mapper.writeValueAsString(userDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto2.getId()))
                .andExpect(jsonPath("$.email").value(userDto2.getEmail()))
                .andExpect(jsonPath("$.name").value(userDto2.getName()));
    }

    @Test
    void getUsers() throws Exception {
        when(userService.getUsers()).thenReturn(userDtos);
        mvc.perform(get("/users")
                        .content(mapper.writeValueAsString(userDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]id").value(userDto1.getId()))
                .andExpect(jsonPath("$[0]email").value(userDto1.getEmail()))
                .andExpect(jsonPath("$[0]name").value(userDto1.getName()))
                .andExpect(jsonPath("$[1]id").value(userDto3.getId()))
                .andExpect(jsonPath("$[1]email").value(userDto3.getEmail()))
                .andExpect(jsonPath("$[1]name").value(userDto3.getName()));
    }

    @Test
    void findUserById() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(userDto1);
        mvc.perform(get("/users/{userId}", 1)
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto1.getId()))
                .andExpect(jsonPath("$.email").value(userDto1.getEmail()))
                .andExpect(jsonPath("$.name").value(userDto1.getName()));
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/users/{userId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void findUserByIdWithWrongId() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenThrow(new NotFoundException("Пользователь не найден"));
        mvc.perform(get("/users/{userId}", 100))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createUserWithEmptyEmail() throws Exception {
        when(userService.createUser(any()))
                .thenThrow(new ValidationException("Поле email заполнено неверно"));
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto4))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}