package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingController bookingController;
    @Mock
    BookingServiceImpl bookingServiceImpl;
    @Autowired
    private MockMvc mockMvc;
    private BookingDto bookingDto;
    private User user;
    private final List<BookingDto> bookingDtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto();
        user = new User();
        user.setId(1L);
        user.setName("TestName");
        user.setEmail("test@mail.ru");
        bookingDto.setBooker(user);
        bookingDto.setStatus(Status.APPROVED);
    }

    @Test
    void getBookingByIdAll() {

        Mockito.when(bookingServiceImpl.getBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingDto);
        BookingDto bookingDto1 = bookingController.getBookingById(Mockito.anyLong(), Mockito.anyLong());

        assertThat(bookingDto1.getBooker(), equalTo(bookingDto.getBooker()));

        Mockito.when(bookingServiceImpl.getBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new NotFoundException("Пользователь с id = " + 1 + " не найден"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> bookingServiceImpl.getBookingById(1L, 1L));

        assertThat(notFoundException.getMessage(),
                equalTo("Пользователь с id = " + 1 + " не найден"));
    }

    @Test
    void createBookingTest() {

        Mockito.when(bookingServiceImpl.createBooking(Mockito.any(), Mockito.anyLong()))
                .thenReturn(bookingDto);
        BookingDto bookingDto1 = bookingController.createBooking(Mockito.anyLong(), Mockito.any());

        assertThat(bookingDto1.getBooker(), equalTo(bookingDto.getBooker()));

        Mockito.when(bookingServiceImpl.createBooking(bookingDto, 1L))
                .thenThrow(new NotFoundException("Пользователь не найден"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> bookingServiceImpl.createBooking(bookingDto, 1L));

        assertThat(notFoundException.getMessage(),
                equalTo("Пользователь не найден"));

        Mockito.when(bookingServiceImpl.createBooking(bookingDto, 2L))
                .thenThrow(new NotFoundException("Вещь с данным id не найдена"));
        NotFoundException notFoundException2 = assertThrows(NotFoundException.class,
                () -> bookingServiceImpl.createBooking(bookingDto, 2L));

        assertThat(notFoundException2.getMessage(),
                equalTo("Вещь с данным id не найдена"));
    }

    @Test
    void approvedBookingTest() {
        Mockito.when(bookingServiceImpl.approvedBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(bookingDto);
        BookingDto bookingDto1 = bookingController.approvedBooking(1L, 1, true);

        assertThat(bookingDto1.getStatus(), equalTo(bookingDto.getStatus()));

        bookingDto.setStatus(Status.REJECTED);
        Mockito.when(bookingServiceImpl.approvedBooking(1L, 1, false))
                .thenReturn(bookingDto);
        bookingDto.setStatus(Status.CANCELED);
        BookingDto bookingDto2 = bookingController.approvedBooking(1L, 1, false);

        assertThat(bookingDto2.getStatus(), equalTo(bookingDto.getStatus()));

        Mockito.when(bookingServiceImpl.approvedBooking(1L, 3L, true))
                .thenThrow(new NotFoundException("Бронирование не найдено"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> bookingServiceImpl.approvedBooking(1L, 3L, true));

        assertThat(notFoundException.getMessage(),
                equalTo("Бронирование не найдено"));
    }

    @Test
    void getAllBookingForUser() {
        bookingDtos.add(bookingDto);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(bookingServiceImpl.getAllBookingForUser(Mockito.anyLong(), Mockito.any(),
                        Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(bookingDtos);
        List<BookingDto> bookingDtos1 = bookingController.getAllBookingForUser(Mockito.anyLong(), Mockito.any(),
                Mockito.anyInt(), Mockito.anyInt());

        assertThat(bookingDtos1.size(), equalTo(bookingDtos.size()));
    }
}
