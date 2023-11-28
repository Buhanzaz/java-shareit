package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    ItemRequestDto toDto(ItemRequest itemRequest);

    @Mapping(target = "requestId", source = "itemRequest.id")
    ItemDto itemToItemDto(Item item);

    default ItemRequest toModel(ItemRequestDto dto, User creator) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .creator(creator)
                .created(LocalDateTime.now())
                .build();
    }
}
