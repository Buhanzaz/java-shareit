package shareit.gateway.validation;


import shareit.gateway.booking.enums.State;
import shareit.gateway.exception.EnumException;

public final class Validator {

    public static void validationTypeBooking(String state) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new EnumException(String.format("Unknown state: %s", state));
        }
    }
}
