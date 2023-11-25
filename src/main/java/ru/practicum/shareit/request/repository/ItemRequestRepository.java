package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> searchItemRequestByCreator_IdOrderByCreatedAsc(Long creatorId, Pageable page);
    Optional<ItemRequest> searchItemRequestByIdOrderByCreatedAsc(Long creatorId);
}