package shareit.server.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import shareit.server.item.model.Item;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item as i join fetch i.user as u where u.id = :userId and i.id = :itemId")
    Optional<Item> findItemByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);

    @Query("select i from Item as i join fetch i.user as u where u.id = :userId")
    List<Item> findAllItemByUser(@Param("userId") Long userId);

    @Query("select i from Item as i " +
            "where ((upper(i.name) like upper(concat('%', :text, '%')) " +
            "or upper(i.description) like upper(concat('%', :text, '%')))" +
            "and i.available = true )")
    List<Item> searchItem(@Param("text") String text, Pageable page);

    @Query("select i from Item as i join fetch i.user where i.id = :itemId")
    Optional<Item> findByIdFetchEgle(@Param("itemId") Long itemId);
}
