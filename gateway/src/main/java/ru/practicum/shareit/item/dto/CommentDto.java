package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.validate.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentDto {
    @Size(max = 512, groups = Create.class, message = "Слишком большой комментарий")
    @NotBlank(groups = Create.class, message = "Пустой комментарий")
    private String text;
}