package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithoutItemDto;
import ru.practicum.shareit.booking.dto.ClientRequestBookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class BookingMapper {
    public abstract BookingDto toDto(Booking booking);

    @Mapping(target = "bookerId", expression = "java(booking.getBooker().getId())")
    public abstract BookingWithoutItemDto bookingToWithoutItemDto(Booking booking);

    public Booking clientRequestDtoToModel(ClientRequestBookingDto dto, User booker, Item item, Status status) {
        if (dto == null) {
            return null;
        }
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(item)
                .booker(booker)
                .status(status)
                .build();
    }
}
