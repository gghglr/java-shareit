package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemServiceImpl itemService;
    @Autowired
    MockMvc mock;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setName("testItemName");
        itemDto.setDescription("testItemDesc");
    }

    @Test
    void getItemById() {

        Mockito.when(itemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);
        ItemDto itemDto1 = itemController.getItemById(Mockito.anyLong(), Mockito.anyLong());

        assertThat(itemDto1.getDescription(), equalTo(itemDto.getDescription()));

        Mockito.when(itemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new NotFoundException("Пользователь не найден"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(Mockito.anyLong(), Mockito.anyLong()));

        assertThat(notFoundException.getMessage(),
                equalTo("Пользователь не найден"));
    }

    @Test
    void createItem() {

        Mockito.when(itemService.createItem(Mockito.anyLong(), Mockito.any()))
                .thenReturn(itemDto);
        ItemDto itemDto1 = itemController.createItem(Mockito.anyLong(), Mockito.any());

        assertThat(itemDto1.getDescription(), equalTo(itemDto.getDescription()));

        Mockito.when(itemService.createItem(Mockito.anyLong(), Mockito.any()))
                .thenThrow(new NotFoundException("Пользователь не найден"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemController.createItem(Mockito.anyLong(), Mockito.any()));

        assertThat(notFoundException.getMessage(),
                equalTo("Пользователь не найден"));
    }

    @Test
    void updateItem() {
        itemDto.setDescription("update");
        Mockito.when(itemService.updateItem(Mockito.anyLong(), Mockito.any(), Mockito.anyLong()))
                .thenReturn(itemDto);
        ItemDto itemDto1 = itemController.updateItem(Mockito.anyLong(), Mockito.any(), Mockito.anyLong());

        assertThat(itemDto1.getDescription(), equalTo(itemDto.getDescription()));

        Mockito.when(itemService.updateItem(Mockito.anyLong(), Mockito.any(), Mockito.anyLong()))
                .thenThrow(new NotFoundException("Пользователь не найден"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemController.updateItem(Mockito.anyLong(), Mockito.any(), Mockito.anyLong()));

        assertThat(notFoundException.getMessage(),
                equalTo("Пользователь не найден"));
    }

    @Test
    void getAllItemForOwner() {

        List<ItemDto> items = new ArrayList<>();
        Mockito.when(itemService.getAllItemForOwner(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyLong()))
                .thenReturn(items);
        List<ItemDto> items2 = itemController.getAllItemForOwner(1L, 1, 3);

        assertThat(items.size(), equalTo(items2.size()));

        Mockito.when(itemService.getAllItemForOwner(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyLong()))
                .thenThrow(new NotFoundException("Пользователь не найден"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemController.getAllItemForOwner(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()));

        assertThat(notFoundException.getMessage(),
                equalTo("Пользователь не найден"));
    }

    @Test
    void getItemForBooker() {
        List<ItemDto> items = new ArrayList<>();
        Mockito.when(itemService.getItemForBooker(Mockito.anyString(), Mockito.anyLong(),
                        Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(items);
        List<ItemDto> items2 = itemController.searchItem(1L, "", 1, 3);

        assertThat(items.size(), equalTo(items2.size()));

        Mockito.when(itemService.getItemForBooker(Mockito.anyString(), Mockito.anyLong(),
                Mockito.anyInt(), Mockito.anyInt())).thenThrow(new NotFoundException("Пользователь не найден"));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemController.searchItem(Mockito.anyLong(), Mockito.anyString(),
                        Mockito.anyInt(), Mockito.anyInt()));

        assertThat(notFoundException.getMessage(),
                equalTo("Пользователь не найден"));
    }
}
