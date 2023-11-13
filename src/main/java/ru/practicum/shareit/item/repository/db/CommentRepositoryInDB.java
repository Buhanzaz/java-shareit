package ru.practicum.shareit.item.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepositoryInDB extends JpaRepository<Comment, Long> {
    List<Comment> findByItem_Id(Long itemId);

    List<Comment> findByItem_UserId(Long userId);
}
