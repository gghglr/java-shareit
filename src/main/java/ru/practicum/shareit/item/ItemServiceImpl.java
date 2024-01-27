package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepositoryImpl itemRepositoryImpl) {
        this.itemRepository = itemRepositoryImpl;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        return ItemMapper.toItemDto(itemRepository.createItem(userId, ItemMapper.toItem(itemDto)));

    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        return ItemMapper.toItemDto(itemRepository.updateItem(userId, ItemMapper.toItem(itemDto), itemId));
    }

    @Override
    public List<ItemDto> getAllItemForOwner(Long userId) {
        return itemRepository.getAllItemForOwner(userId).stream()
                .map(x -> ItemMapper.toItemDto(x)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItem(userId, text).stream()
                .map(x -> ItemMapper.toItemDto(x)).collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
    }
}
