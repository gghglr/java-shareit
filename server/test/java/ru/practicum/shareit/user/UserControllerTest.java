package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @MockBean
    UserServiceImpl userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    private UserDto userDto = new UserDto();
    private User user = new User();

    @BeforeEach
    void setUp() {
        userDto.setId(1);
        userDto.setEmail("test@mail.ru");
        userDto.setName("testName");
        user = UserMapper.toUser(userDto);
    }

    @Test
    void createUser() throws Exception {
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(userDto);
        mockMvc.perform(post("/users").content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.name").value(userDto.getName()));
    }

    @Test
    void getUserById() throws Exception {

        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(userDto);
        mockMvc.perform(get("/users/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.name").value(userDto.getName()));
    }

}
