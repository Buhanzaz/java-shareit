package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository {
    Item addItem(Item item);

    Item updateItem(Long itemId, Long userid, Item dto);

    boolean isItemInBd(Long itemId);

    boolean isItemOwner(Long itemId, Long userid);

    Item getItemById(Long itemId);

    List<Item> getAllItemsOwner(Long userId);

    List<Item> itemSearch(String itemName);
}
