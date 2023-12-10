package shareit.server.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shareit.server.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> searchItemRequestByCreatorId(Long creatorId, Pageable page);

    Optional<ItemRequest> searchItemRequestById(Long creatorId);
}