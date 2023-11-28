package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; //Id вещи.

    @Column(name = "name", nullable = false)
    String name; //Название вещи.

    @Column(name = "description", nullable = false)
    String description; //Описание.

    @Column(name = "available")
    Boolean available; //Статус доступности вещи True - доступно, False - нет.

    @ManyToOne
    @JoinColumn(name = "owner_id")
    User user; //Владелец вещи owner_id == userId.

    @ManyToOne
    @JoinColumn(name="request_id")
    ItemRequest itemRequest;
}
