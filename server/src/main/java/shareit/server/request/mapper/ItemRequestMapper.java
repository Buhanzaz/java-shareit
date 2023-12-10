package shareit.server.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shareit.server.item.dto.ItemDto;
import shareit.server.item.model.Item;
import shareit.server.request.dto.ItemRequestDto;
import shareit.server.request.model.ItemRequest;
import shareit.server.user.model.User;

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
