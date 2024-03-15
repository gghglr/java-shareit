package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.RequestServiceImpl;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestController itemRequestController;
    @Mock
    private RequestServiceImpl requestServiceImpl;
    @Autowired
    MockMvc mock;
    private RequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new RequestDto();
        requestDto.setDescription("RequestTestDesc");
        requestDto.setAvailable(true);
    }

    @Test
    void getRequestById() {

        Mockito.when(requestServiceImpl.getRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(requestDto);
        RequestDto requestDto1 = itemRequestController.getRequestById(Mockito.anyLong(), Mockito.anyLong());

        assertThat(requestDto1.getDescription(), equalTo(requestDto.getDescription()));

        Mockito.when(requestServiceImpl.getRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new NotFoundException("Такого пользователя не существует"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestController.getRequestById(Mockito.anyLong(), Mockito.anyLong()));

        assertThat(notFoundException.getMessage(),
                equalTo("Такого пользователя не существует"));
    }

    @Test
    void createRequest() {

        Mockito.when(requestServiceImpl.createRequest(Mockito.any(), Mockito.anyLong()))
                .thenReturn(requestDto);
        RequestDto requestDto1 = itemRequestController.createRequest(Mockito.anyLong(), Mockito.any());

        assertThat(requestDto1.getDescription(), equalTo(requestDto.getDescription()));

        Mockito.when(requestServiceImpl.createRequest(Mockito.any(), Mockito.anyLong()))
                .thenThrow(new NotFoundException("Такого пользователя не существует"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestController.createRequest(Mockito.anyLong(), Mockito.any()));

        assertThat(notFoundException.getMessage(),
                equalTo("Такого пользователя не существует"));
    }

    @Test
    void getRequests() {

        List<RequestDto> requests = new ArrayList<>();
        requests.add(requestDto);

        Mockito.when(requestServiceImpl.getRequests(Mockito.anyLong()))
                .thenReturn(requests);
        List<RequestDto> requests1 = itemRequestController.getRequests(Mockito.anyLong());

        assertThat(requests1.size(), equalTo(requests.size()));

        Mockito.when(requestServiceImpl.getRequests(Mockito.anyLong()))
                .thenThrow(new NotFoundException("Такого пользователя не существует"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestController.getRequests(Mockito.anyLong()));

        assertThat(notFoundException.getMessage(),
                equalTo("Такого пользователя не существует"));
    }

    @Test
    void getCurrentCountOfRequests() {

        List<RequestDto> requests = new ArrayList<>();
        requests.add(requestDto);

        Mockito.when(requestServiceImpl.getCurrentCountOfRequests(Mockito.anyInt(), Mockito.anyInt(),
                        Mockito.anyLong()))
                .thenReturn(requests);
        List<RequestDto> requests1 = itemRequestController.getCurrentCountOfRequests(Mockito.anyLong(),
                Mockito.anyInt(), Mockito.anyInt());

        assertThat(requests1.size(), equalTo(requests.size()));

        Mockito.when(requestServiceImpl.getCurrentCountOfRequests(Mockito.anyInt(), Mockito.anyInt(),
                        Mockito.anyLong()))
                .thenThrow(new NotFoundException("Такого пользователя не существует"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestController.getCurrentCountOfRequests(Mockito.anyLong(),
                        Mockito.anyInt(), Mockito.anyInt()));

        assertThat(notFoundException.getMessage(),
                equalTo("Такого пользователя не существует"));
    }
}
