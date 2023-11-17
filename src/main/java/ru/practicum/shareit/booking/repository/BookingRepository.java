package ru.practicum.shareit.booking.repository;

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

    List<Booking> findByBookerIdOrderByStartDesc(Long bookingId);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookingId,
                                                                              LocalDateTime end,
                                                                              LocalDateTime start);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookingId, LocalDateTime time);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long bookingId, LocalDateTime time);

    List<Booking> findByBookerIdAndStartIsAfterAndStatusIsOrderByStartDesc(Long bookerId,
                                                                           LocalDateTime start,
                                                                           Status status);

    List<Booking> findByBookerIdAndStatusIsOrderByStartDesc(Long bookingId, Status status);

    List<Booking> findByItem_User_IdOrderByStartDesc(Long userId);

    List<Booking> findByItem_User_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId,
                                                                                  LocalDateTime start,
                                                                                  LocalDateTime end);

    List<Booking> findByItem_User_IdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime end);

    List<Booking> findByItem_User_IdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime start);

    List<Booking> findByItem_User_IdAndStartIsAfterAndStatusIsOrderByStartDesc(Long userId,
                                                                               LocalDateTime start,
                                                                               Status status);

    List<Booking> findByItem_User_IdAndStatusIsOrderByStartDesc(Long userId, Status status);

    List<Booking> findBookingsByItem_Id(Long itemId);

    List<Booking> findByItem_IdAndStatusNotIn(Long itemId, Collection<Status> status);

    List<Booking> findByItem_UserId(Long userId);

    List<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(Long itemId, Long bookerId, Status status, LocalDateTime end);
}
