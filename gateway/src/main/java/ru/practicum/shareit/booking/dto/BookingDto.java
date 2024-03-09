package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.FutureOrPresent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validate.Create;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private long itemId;
    @FutureOrPresent(groups = Create.class)
    private LocalDateTime start;
    private LocalDateTime end;
}