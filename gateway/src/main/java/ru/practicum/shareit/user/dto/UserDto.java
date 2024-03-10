package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.validate.Create;
import ru.practicum.shareit.validate.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {

    private long id;
    @Size(max = 255, groups = {Create.class, Update.class})
    @NotBlank(message = "Поле name пустое")
    private String name;
    @NotBlank(message = "после email пустое", groups = Create.class)
    @Email(message = "поле email некорректно", groups = {Create.class, Update.class})
    @Size(max = 255, groups = {Create.class, Update.class})
    private String email;
}

