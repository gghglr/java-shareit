package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner_idOrderByIdAsc(Pageable pageable, long userId);

    @Query(value = "SELECT i FROM Item AS i WHERE ((UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))) AND i.available IS TRUE)")
    List<Item> getItemForBooker(String text, Pageable pageable);

    @Query(value = "SELECT i FROM Item AS i WHERE i.requestId = ?1")
    List<Item> findByRequestId(long requestId);
}