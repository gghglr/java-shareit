package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker_IdOrderByStartDesc(long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking AS b WHERE b.booker.id = ?1 AND " +
            "CURRENT_TIME BETWEEN b.start AND b.end ORDER BY b.start DESC")
    List<Booking> findCurrentBookerForUser(long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking AS b WHERE b.booker.id = ?1 AND " +
            "CURRENT_TIME > b.end ORDER BY b.start DESC")
    List<Booking> getPastBooking(long userId, Pageable pageable);

    List<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(long userId, LocalDateTime startTime, Pageable pageable);

    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(long userId, Status status, Pageable pageable);

    List<Booking> findByItem_Owner_IdOrderByStartDesc(long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND " +
            "CURRENT_TIME BETWEEN b.start AND b.end ORDER BY b.start DESC")
    List<Booking> findCurrentBookerForOwner(long userId, Pageable pageable);

    List<Booking> findByItem_Owner_IdAndStartAfterOrderByStartDesc(long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(long userId, Status status, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND " +
            "CURRENT_TIME > b.end ORDER BY b.start DESC")
    List<Booking> findPastBookerForOwner(long idUser, Pageable pageable);

    @Query(value = "SELECT b FROM Booking AS b WHERE ((b.item.id = ?1) AND " +
            "((b.start > ?2 AND b.start < ?3) OR (b.end > ?2 AND b.end < ?3))) ORDER BY b.start DESC")
    List<Booking> findBookingByItemToFree(long itemId, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.item.owner.id = ?2 " +
            "AND b.status = 'APPROVED' ORDER BY b.start DESC")
    List<Booking> findBookingByItemAndOwner(long itemId, long ownerId);

    @Query(value = "SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.booker.id = ?2 " +
            "AND b.status = 'APPROVED' AND b.end < CURRENT_TIME ORDER BY b.start DESC")
    List<Booking> findBookingByItem(long itemId, long bookerId);

    @Query(value = "SELECT count(b.item.owner.id) FROM Booking AS b WHERE b.item.owner.id = ?1")
    Integer getCountForOwner(long userId);

    @Query(value = "SELECT count(b.booker.id) FROM Booking AS b WHERE b.booker.id = ?1")
    Integer getCount(long userId);
}
