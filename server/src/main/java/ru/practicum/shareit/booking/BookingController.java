package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос на создание аренды");
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approvedBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable("bookingId") long bookingId,
                                      @RequestParam(name = "approved") boolean isApproved) {
        log.info("Получен запрос на обновление статуса");
        return bookingService.approvedBooking(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable("bookingId") long bookingId) {
        log.info("Получен запрос о получении информации по конкретному бронированию");
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getAllBookingForUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                 @RequestParam(value = "from", defaultValue = "0") int from,
                                                 @RequestParam(value = "size", defaultValue = "100") int size) {
        log.info("Получен запрос на получение списка всех бронирований пользователя");
        return bookingService.getAllBookingForUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingForOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                  @RequestParam(value = "from", defaultValue = "0") int from,
                                                  @RequestParam(value = "size", defaultValue = "100") int size) {
        log.info("Получен запрос на получение списка всех бронирований для владельца");
        return bookingService.getAllBookingForOwner(userId, state, from, size);
    }
}
