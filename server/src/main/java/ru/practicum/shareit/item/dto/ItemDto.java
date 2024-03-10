package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private long id;
    private String name;
    private String description;
    private boolean available;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
    private long requestId;
}

