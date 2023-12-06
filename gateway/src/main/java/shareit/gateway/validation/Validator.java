package shareit.gateway.validation;


import shareit.gateway.booking.dto.ClientRequestBookingDto;
import shareit.gateway.booking.enums.State;
import shareit.gateway.exception.DataTimeException;
import shareit.gateway.exception.EnumException;

public final class Validator {

    public static void validationTypeBooking(String state) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new EnumException(String.format("Unknown state: %s", state));
        }
    }

    public static void validationTimeFromDto(ClientRequestBookingDto dto) {
        if (dto.getEnd().isBefore(dto.getStart()) || dto.getStart().equals(dto.getEnd()))
            throw new DataTimeException("Ошибка! Начало бронирования не может быть позже конца бронирования!");
    }
}
