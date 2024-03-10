package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validate.Create;
import ru.practicum.shareit.validate.Update;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление вещи");
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Validated(Update.class) @RequestBody ItemDto itemDto,
                                         @PathVariable("itemId") Long itemId) {
        log.info("Получен запрос на обновление информации о вещи");
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(value = "from", defaultValue = "0") int from,
                                                     @RequestParam(value = "size", defaultValue = "100") int size) {
        log.info("Владелец запросил список своих вещей");
        return itemClient.getAllItemForOwner(from, size, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(name = "text", defaultValue = "") String text,
                                             @RequestParam(value = "from", defaultValue = "0") int from,
                                             @RequestParam(value = "size", defaultValue = "100") int size) {
        log.info("Запрос на поиск вещи по содержанию");
        return itemClient.searchItem(text, userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable("itemId") long itemId,
                                              @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на поиск вещи по id");
        return itemClient.getItemById(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @Validated(Create.class) @RequestBody CommentDto commentDto,
                                                @PathVariable("itemId") long itemId) {
        log.info("Получен запрос на добавление комментария");
        return itemClient.createComment(userId, commentDto, itemId);
    }
}
