package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class Booking {
    private final Long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Long item;
    private final Long booker;
    private final Status status;
}