package com.example.hotel.repository;

import com.example.hotel.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @Query("SELECT u FROM User u")
    Slice<User> fetchAll(Specification<User> spec, Pageable pageable);

    Optional<User> findByUsername(String username);

    @Query("SELECT u.id FROM User u " +
            "WHERE LOWER(u.username) = LOWER(:username) " +
            "AND LOWER(u.email) = LOWER(:email)")
    Optional<UUID> findUserIdByUsernameAndEmail(@Param("username") String username,
                                                @Param("email") String email);
}
