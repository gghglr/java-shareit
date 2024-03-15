package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.validate.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ItemRequestDto {
    private long id;

    @NotBlank(message = "Описание запроса пустое", groups = Create.class)
    @Size(max = 512, groups = Create.class)
    private String description;
}