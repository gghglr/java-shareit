package ru.practicum.shareit.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /*@Query(value = "SELECT c FROM Comment c WHERE c.item.id = ?1")
    List<Comment> findByItem(long itemId);*/

    List<Comment> findByItem_Id(long itemId);
}
