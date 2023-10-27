package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository {
    Item addItem(Item item);

    Item getItemById(Long itemId);

    List<Item> getAllItemsOwner(Long userId);

    List<Item> itemSearch(String itemName);
}
