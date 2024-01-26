package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, ItemDto itemDto, long itemId);

    List<ItemDto> getAllItemForOwner(Long userId);

    List<ItemDto> searchItem(Long userId, String text);

    ItemDto getItemById(Long itemId);
}
