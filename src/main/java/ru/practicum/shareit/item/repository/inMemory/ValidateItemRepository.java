package ru.practicum.shareit.item.repository.inMemory;

public interface ValidateItemRepository {
    boolean isItemInBd(Long itemId);

    boolean isItemOwner(Long itemId, Long userid);
}
