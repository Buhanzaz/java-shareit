package ru.practicum.shareit.item.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryItemRepositoryImpl implements ItemRepository {

    final HashMap<Long, Item> items = new HashMap<>();

    Long id = 1L;

    @Override
    public Item addItem(Item item) {
        item.setId(id++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, Long userid, Item item) {
        Item chengeItem = getItemById(itemId);
        Optional<String> name = Optional.ofNullable(item.getName());
        Optional<String> description = Optional.ofNullable(item.getDescription());
        Optional<Boolean> available = Optional.ofNullable(item.getAvailable());

        name.ifPresent(chengeItem::setName);
        description.ifPresent(chengeItem::setDescription);
        available.ifPresent(chengeItem::setAvailable);

        return chengeItem;
    }

    @Override
    public boolean isItemInBd(Long itemId) {
        Optional<Item> item = Optional.ofNullable(items.get(itemId));
        return item.isPresent();
    }

    @Override
    public boolean isItemOwner(Long itemId, Long userid) {
        return items.get(itemId)
                .getUserId()
                .equals(userid);
    }


    public Item getItemById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItemsOwner(Long userId) {
        return items.values().stream()
                .filter(item -> item.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> itemSearch(String itemName) {
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(itemName.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(itemName.toLowerCase())) &&
                        item.getAvailable())
                .collect(Collectors.toList());
    }
}
