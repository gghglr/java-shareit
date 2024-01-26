package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.Forbidden;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private Long id = 1L;
    private Map<Long, Item> items = new HashMap<>(); //айди вещи и вещь
    private Map<Long, List<Long>> itemsPerOwner = new HashMap<>(); //айди владельца и список
    private final UserRepositoryImpl userRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userRepository.getUserById(userId);
        if (itemDto.getId() == null) {
            itemDto.setId(id);
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);
        List<Long> itemList;
        if (itemsPerOwner.containsKey(userId)) {
            itemList = itemsPerOwner.get(userId);
        } else {
            itemList = new ArrayList<>();
        }
        itemList.add(item.getId());
        itemsPerOwner.put(userId, itemList);
        items.put(item.getId(), item);
        id++;
        return itemDto;
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        if (items.get(itemId).getOwner() == userId) {
            if (itemDto.getDescription() != null && itemDto.getName() != null) {
                items.get(itemId).setName(itemDto.getName());
                items.get(itemId).setDescription(itemDto.getDescription());
                items.get(itemId).setAvailable(itemDto.isAvailable());
            } else if (itemDto.getDescription() != null && itemDto.getName() == null) {
                items.get(itemId).setDescription(itemDto.getDescription());
            } else if (itemDto.getDescription() == null && itemDto.getName() != null) {
                items.get(itemId).setName(itemDto.getName());
            } else {
                items.get(itemId).setAvailable(itemDto.isAvailable());
            }
        } else {
            throw new Forbidden("Только владелец имеет право изменять параметры вещи");
        }
        return ItemMapper.toItemDto(items.get(itemId));
    }

    @Override
    public List<ItemDto> getAllItemForOwner(Long userId) {
        List<Long> itemIdForOwner = itemsPerOwner.get(userId);
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Long id : itemIdForOwner) {
            itemsDto.add(ItemMapper.toItemDto(items.get(id)));
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String text) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase())
                    || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    && item.isAvailable()) {
                itemsDto.add(ItemMapper.toItemDto(item));
            }
        }
        return itemsDto;
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        try {
            return ItemMapper.toItemDto(items.get(itemId));
        } catch (NotFoundException e) {
            throw new NotFoundException("Вещи не существует");
        }
    }
}
