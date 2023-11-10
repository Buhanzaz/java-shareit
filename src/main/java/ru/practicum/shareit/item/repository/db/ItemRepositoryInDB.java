package ru.practicum.shareit.item.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ItemRepositoryInDB extends JpaRepository<Item, Long> {
    @Query("select i from Item as i join fetch i.user as u where u.id = :userId and i.id = :itemId")
    Optional<Item> findItemByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);

    @Query("select i from Item as i join fetch i.user as u where u.id = :userId")
    List<Item> findAllItemByUser(@Param("userId") Long userId);

    @Query("select i from Item as i " +
            "where ((upper(i.name) like upper(concat('%', :text, '%')) " +
            "or upper(i.description) like upper(concat('%', :text, '%')))" +
            "and i.available = true )")
    List<Item> searchItem(@Param("text") String text);
}
