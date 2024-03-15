package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto getItemById(Long itemId, long userId);

    List<ItemDto> getAllItemForOwner(int from, int size, long userId);

    List<ItemDto> getItemForBooker(String text, long userId, int from, int size);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId);

    CommentDto createComment(long userId, CommentDto commentDto, long itemId);
}
