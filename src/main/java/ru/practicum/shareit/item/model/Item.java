package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    Long id; //Id вещи.

    String name; //Название вещи.

    String description; //Описание.

    Boolean available; //Статус доступности вещи True - доступно, False - нет.

    Long userId; //Владелец вещи ownerId == userId.

    @JsonIgnore
    Boolean isRequest; //True - вещь создана другим пользователем, False - владельцем вещи.

    Set<Long> reviews; //Собраны id отзывов.
}
