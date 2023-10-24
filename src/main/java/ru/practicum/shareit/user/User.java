package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;


/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    long id;

    String name;

    String email;
}
