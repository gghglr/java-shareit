package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepositoryImpl itemRepositoryImpl) {
        this.itemRepository = itemRepositoryImpl;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        return itemRepository.createItem(userId, itemDto);
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        return itemRepository.updateItem(userId, itemDto, itemId);
    }

    @Override
    public List<ItemDto> getAllItemForOwner(Long userId) {
        return itemRepository.getAllItemForOwner(userId);
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String text) {
        if(text.isBlank()){
            return new ArrayList<>();
        }
        return itemRepository.searchItem(userId, text);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemRepository.getItemById(itemId);
    }
}
