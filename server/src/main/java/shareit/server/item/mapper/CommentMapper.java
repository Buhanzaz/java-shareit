package shareit.server.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shareit.server.item.dto.CommentDto;
import shareit.server.item.model.Comment;
import shareit.server.item.model.Item;
import shareit.server.user.model.User;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {
    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    public abstract CommentDto commentToDto(Comment comment);

    public Comment commentDtoToModel(CommentDto dto, User user, Item item, LocalDateTime created) {
        return Comment.builder()
                .text(dto.getText())
                .item(item)
                .author(user)
                .created(created)
                .build();
    }
}
