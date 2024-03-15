package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestDto {

    private long id;
    private String name;
    private String description;
    private LocalDateTime created;
    private int itemId;
    private boolean available;
    private long userId;
    @Transient
    private List<Item> items = new ArrayList<>();
}
