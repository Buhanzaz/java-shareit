package shareit.geteway.validation;

import shareit.geteway.booking.dto.ClientRequestBookingDto;
import shareit.geteway.booking.enums.State;
import shareit.geteway.exception.DataTimeException;
import shareit.geteway.exception.EnumException;

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
