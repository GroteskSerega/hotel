package com.example.hotel.repository;

import com.example.hotel.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>, JpaSpecificationExecutor<Booking> {

//    @EntityGraph(attributePaths = {"user", "room"})
//    Page<Booking> findAll(Specification<Booking> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "room"})
    @Query("SELECT b FROM Booking b")
    Slice<Booking> fetchAll(Specification<Booking> spec, Pageable pageable);

    @Query("SELECT count(*) FROM Booking b " +
            "WHERE b.room.id = :roomId " +
            "AND b.checkInDate < :checkOutDate " +
            "AND b.checkOutDate > :checkInDate")
    Long countOverlappingBooking(@Param("roomId") UUID roomId,
                                    @Param("checkInDate") Instant checkInDate,
                                    @Param("checkOutDate") Instant checkOutDate);
}
