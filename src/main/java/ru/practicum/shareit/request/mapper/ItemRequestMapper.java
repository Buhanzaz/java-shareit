package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    ItemRequestDto toDto(ItemRequest itemRequest);

    default ItemRequest toModel(ItemRequestDto dto, User creator) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .creator(creator)
                .created(LocalDateTime.now())
                .build();
    }
}
