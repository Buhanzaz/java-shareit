package shareit.server.item.mapper;

import org.mapstruct.*;
import shareit.server.item.dto.ItemDto;
import shareit.server.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toModelItem(ItemDto dto);

    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "requestId", source = "itemRequest.id")
    ItemDto toDtoItem(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "itemRequest", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItem(@MappingTarget Item item, ItemDto dto);
}
