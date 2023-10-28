package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addItem(Item item);

    Item getItemById(Long itemId);

    List<Item> getAllItemsOwner(Long userId);

    List<Item> itemSearch(String itemName);
}
