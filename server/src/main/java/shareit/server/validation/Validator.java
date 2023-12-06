package shareit.server.validation;

import shareit.server.booking.enums.State;
import shareit.server.exception.EnumException;

public final class Validator {

    public static void validateTypeBooking(String state) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new EnumException(String.format("Unknown state: %s", state));
        }
    }
}
