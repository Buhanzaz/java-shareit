package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ClientRequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemRepositoryInDB;

@Mapper(componentModel = "spring")
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BookingMapper {

    @Autowired
    ItemRepositoryInDB itemRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booker", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "item", source = "itemId", qualifiedByName = "fromLongToItem")
    public abstract Booking toModel(ClientRequestBookingDto dto);

    public abstract BookingDto toDto(Booking booking);

    @Named(value = "fromLongToItem")
    public Item fromLongToItem(Long ItemId) {
        return itemRepository.findByIdFetchEgle(ItemId)
                .orElseThrow(() -> new NotFoundException("Вещи с таким id не найдено"));
    }
}
