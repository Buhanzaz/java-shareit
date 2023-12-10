package shareit.server.booking.mapper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shareit.server.booking.dto.BookingDto;
import shareit.server.booking.dto.BookingWithoutItemDto;
import shareit.server.booking.dto.ClientRequestBookingDto;
import shareit.server.booking.enums.Status;
import shareit.server.booking.model.Booking;
import shareit.server.item.model.Item;
import shareit.server.user.model.User;

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
