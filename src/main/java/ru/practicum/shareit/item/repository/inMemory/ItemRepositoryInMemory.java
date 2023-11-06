package ru.practicum.shareit.item.repository.inMemory;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepositoryInMemory {
    Item addItem(Item item);

    Item getItemById(Long itemId);

    List<Item> getAllItemsOwner(Long userId);

    List<Item> itemSearch(String itemName);
}
