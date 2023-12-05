package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking as b " +
            "join fetch b.item as i " +
            "join fetch i.user as u " +
            "join fetch b.booker as boo " +
            "where (u.id = :userId or boo.id = :userId) and b.id = :bookingId")
    Optional<Booking> findBookingForAuthorBookingOrOwnerItem(@Param("userId") Long userId, @Param("bookingId") Long bookingId);

    Optional<Booking> findBookingByIdAndItem_User_Id(Long bookingId, Long userId);

    List<Booking> findByBooker_Id(Long bookerId, Pageable page);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookingId,
                                                              LocalDateTime end,
                                                              LocalDateTime start, Pageable page);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookingId, LocalDateTime time, Pageable page);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookingId, LocalDateTime time, Pageable page);

    List<Booking> findByBookerIdAndStartIsAfterAndStatusIs(Long bookerId,
                                                           LocalDateTime start,
                                                           Status status, Pageable page);

    List<Booking> findByBookerIdAndStatusIs(Long bookingId, Status status, Pageable page);

    List<Booking> findByItem_User_Id(Long userId, Pageable page);

    List<Booking> findByItem_User_IdAndStartIsBeforeAndEndIsAfter(Long userId,
                                                                  LocalDateTime start,
                                                                  LocalDateTime end, Pageable page);

    List<Booking> findByItem_User_IdAndEndIsBefore(Long userId, LocalDateTime end, Pageable page);

    List<Booking> findByItem_User_IdAndStartIsAfter(Long userId, LocalDateTime start, Pageable page);

    List<Booking> findByItem_User_IdAndStartIsAfterAndStatusIs(Long userId,
                                                               LocalDateTime start,
                                                               Status status, Pageable page);

    List<Booking> findByItem_User_IdAndStatusIs(Long userId, Status status, Pageable page);

    List<Booking> findByItem_IdAndStatusNotIn(Long itemId, Collection<Status> status);

    List<Booking> findByItem_UserId(Long userId);

    List<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(Long itemId, Long bookerId, Status status, LocalDateTime end);
}
