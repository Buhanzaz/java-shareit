package ru.practicum.shareit.item.repository.inMemory;

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
class ItemRepositoryInMemoryImplInMemory implements ItemRepositoryInMemory, ValidateItemRepository {
    final HashMap<Long, Item> items = new HashMap<>();

    Long id = 1L;

    @Override
    public Item addItem(Item item) {
        item.setId(id++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public boolean isItemInBd(Long itemId) {
        Optional<Item> item = Optional.ofNullable(items.get(itemId));
        return item.isPresent();
    }

    @Override
    public boolean isItemOwner(Long itemId, Long userid) {
        return items.get(itemId)
                .getUser()
                .getId() == userid;
    }


    @Override
    public Item getItemById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItemsOwner(Long userId) {
        return items.values().stream()
                .filter(item -> item.getUser().getId() == userId)
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
