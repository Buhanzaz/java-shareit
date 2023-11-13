package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    CommentDto toDto(Comment comment);

    Comment toModel(CommentDto dto);
}
