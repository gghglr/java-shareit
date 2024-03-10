package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.validate.Create;
import ru.practicum.shareit.validate.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ItemDto {
    private long id;

    @Size(max = 255, groups = {Create.class, Update.class})
    @NotBlank(message = "поле name пустое", groups = Create.class)
    private String name;

    @Size(max = 512, groups = {Create.class, Update.class})
    @NotBlank(message = "описание пустое", groups = Create.class)
    private String description;

    @NotNull(message = "доступность пустая", groups = Create.class)
    private Boolean available;
    private long requestId;
}