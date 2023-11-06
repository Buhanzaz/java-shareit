package ru.practicum.shareit.item.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoryInDB extends JpaRepository<Item, Long> {
    @Query("select i from Item as i join fetch i.user as u where u.id = :userId and i.id = :itemId")
    Optional<Item> findItemByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);
}
