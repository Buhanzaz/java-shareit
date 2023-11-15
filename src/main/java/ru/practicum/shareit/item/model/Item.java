package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
@Table(name = "items")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; //Id вещи.

    @Column(name = "name")
    String name; //Название вещи.

    @Column(name = "description")
    String description; //Описание.

    @Column(name = "available")
    Boolean available; //Статус доступности вещи True - доступно, False - нет.

    @ManyToOne
    @JoinColumn(name = "owner_id")
    User user; //Владелец вещи owner_id == userId.

    @JsonIgnore
    @Column(name = "request")
    Boolean isRequest; //True - вещь создана другим пользователем, False - владельцем вещи.

}
