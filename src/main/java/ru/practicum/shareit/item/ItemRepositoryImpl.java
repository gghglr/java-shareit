package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.Forbidden;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private Map<Long, Item> items = new HashMap<>(); //айди вещи и вещь
    private Map<Long, List<Long>> itemsPerOwner = new HashMap<>(); //айди владельца и список
    private final UserRepositoryImpl userRepository;
    private long id = 1;

    @Override
    public Item createItem(Long userId, Item item) {
        userRepository.getUserById(userId);
        item.setId(id);
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
        return item;
    }

    @Override
    public Item updateItem(long userId, Item item, long itemId) {
        if (items.get(itemId).getOwner() == userId) {
            if (item.getDescription() != null && item.getName() != null) {
                items.get(itemId).setName(item.getName());
                items.get(itemId).setDescription(item.getDescription());
                items.get(itemId).setAvailable(item.isAvailable());
            } else if (item.getDescription() != null && item.getName() == null) {
                items.get(itemId).setDescription(item.getDescription());
            } else if (item.getDescription() == null && item.getName() != null) {
                items.get(itemId).setName(item.getName());
            } else {
                items.get(itemId).setAvailable(item.isAvailable());
            }
        } else {
            throw new Forbidden("Только владелец имеет право изменять параметры вещи");
        }
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItemForOwner(Long userId) {
        List<Long> itemIdForOwner = itemsPerOwner.get(userId);
        List<Item> itemsForReturn = new ArrayList<>();
        for (Long id : itemIdForOwner) {
            itemsForReturn.add(items.get(id));
        }
        return itemsForReturn;
    }

    @Override
    public List<Item> searchItem(Long userId, String text) {
        List<Item> itemsSearched = new ArrayList<>();
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase())
                    || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    && item.isAvailable()) {
                itemsSearched.add(item);
            }
        }
        return itemsSearched;
    }

    @Override
    public Item getItemById(Long itemId) {
        try {
            return items.get(itemId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Вещи не существует");
        }
    }
}
