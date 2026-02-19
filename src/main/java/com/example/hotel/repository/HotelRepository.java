package com.example.hotel.repository;

import com.example.hotel.entity.Hotel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID>, JpaSpecificationExecutor<Hotel> {

    @Query("SELECT h FROM Hotel h")
    Slice<Hotel> fetchAll(Specification<Hotel> spec, Pageable pageable);
}
