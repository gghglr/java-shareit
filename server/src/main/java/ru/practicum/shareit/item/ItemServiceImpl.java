package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        Optional<User> userOpt = userRepository.findById(userId);
        validateUserFounded(userOpt);
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, userOpt.get())));
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        Optional<User> userOptional = userRepository.findById(userId);
        validateUserFounded(userOptional);
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        validateFoundForItem(itemOptional);
        validateForOwner(userId, itemOptional.get().getOwner().getId(), itemId);
        if (itemDto.getDescription() != null && itemDto.getName() != null) {
            Item item = ItemMapper.toItem(itemDto, userOptional.get());
            item.setId(itemId);
            return ItemMapper.toItemDto(itemRepository.save(item));
        }
        if (itemDto.getDescription() != null) {
            Item item = itemOptional.get();
            item.setDescription(itemDto.getDescription());
            return ItemMapper.toItemDto(itemRepository.save(item));
        }
        if (itemDto.getName() != null) {
            Item item = itemOptional.get();
            item.setName(itemDto.getName());
            return ItemMapper.toItemDto(itemRepository.save(item));
        }
        Item item = itemOptional.get();
        item.setAvailable(itemDto.isAvailable());
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long itemId, long userId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        validateFoundForItem(itemOptional);
        Item item = itemOptional.get();
        List<Comment> comments = commentRepository.findByItem_Id(itemId);
        List<CommentDto> commentDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentDto.add(CommentMapper.toCommentDto(comment));
        }
        if (item.getOwner().getId() != userId) {
            return ItemMapper.toItemCommentDto(ItemMapper.toItemDto(item), commentDto);
        }
        List<Booking> bookings = bookingRepository.findBookingByItemAndOwner(itemId, userId);
        if (bookings.isEmpty()) {
            return ItemMapper.toItemCommentDto(ItemMapper.toItemDto(item), commentDto);
        } else {
            return ItemMapper.toItemCommentDto(ItemMapper.toItemOwnerDto(item, bookings), commentDto);
        }
    }

    @Override
    public List<ItemDto> getAllItemForOwner(int from, int size, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        List<Item> items = itemRepository.findByOwner_idOrderByIdAsc(PageRequest.of(from, size), userId);
        List<ItemDto> itemDto = new ArrayList<>();
        for (Item item : items) {
            List<Booking> bookings = bookingRepository.findBookingByItemAndOwner(item.getId(), item.getOwner().getId());
            if (bookings.isEmpty()) {
                itemDto.add(ItemMapper.toItemDto(item));
            } else {
                itemDto.add(ItemMapper.toItemOwnerDto(item, bookings));
            }
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemForBooker(String text, long userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.getItemForBooker(text.toLowerCase().trim(), PageRequest.of(from, size))
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(long userId, CommentDto commentDto, long itemId) {
        //validateComment(commentDto);
        Optional<User> userOptional = userRepository.findById(userId);
        validateUserFounded(userOptional);
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        validateFoundForItem(itemOptional);
        List<Booking> bookings = bookingRepository.findByItem_IdAndBooker_IdAndStatusEqualsAndStartLessThan(itemId,
                userId, Status.APPROVED, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new ValidationException("Пользователь не бронировал эту вещь");
        }
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto,
                itemOptional.get(), userOptional.get().getName())));
    }

    private void validateFoundForItem(Optional<Item> itemOptional) {
        if (itemOptional.isEmpty()) {
            throw new NotFoundException("Вещь с данным id не найдена");
        }
    }

    private void validateUserFounded(Optional<User> user) {
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void validateForOwner(long userId, long ownerId, long itemId) {
        if (ownerId != userId) {
            throw new NotFoundException("У пользователя с id = " + userId + " нет вещи с id = " + itemId);
        }
    }
}