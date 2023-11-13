package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {
    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    public abstract CommentDto commentToDto(Comment comment);

    public Comment CommentDtoToModel(CommentDto dto, User user, Item item, LocalDateTime created) {
        return Comment.builder()
                .text(dto.getText())
                .item(item)
                .author(user)
                .created(created)
                .build();
    }
}
