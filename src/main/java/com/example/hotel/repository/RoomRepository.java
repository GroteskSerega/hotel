package com.example.hotel.repository;

import com.example.hotel.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID>, JpaSpecificationExecutor<Room> {

    @EntityGraph(attributePaths = {"hotel", "unavailableDates"})
    @Query("SELECT r FROM Room r")
    Slice<Room> fetchAll(Specification<Room> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"hotel", "unavailableDates"})
//    @Query("SELECT r FROM Room r")
    Page<Room> findAll(Specification<Room> spec, Pageable pageable);

    @Query("SELECT count(*) FROM Room r " +
            "JOIN r.unavailableDates d " +
            "WHERE r.id = :roomId " +
            "AND d >= :checkInDate " +
            "AND d <= :checkOutDate")
    Long countUnavailableDatesByRoomId(@Param("roomId") UUID roomId,
                                       @Param("checkInDate") Instant checkInDate,
                                       @Param("checkOutDate") Instant checkOutDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Room r WHERE r.id = :id")
    Optional<Room> findByIdWithLock(@Param("id") UUID id);
}
