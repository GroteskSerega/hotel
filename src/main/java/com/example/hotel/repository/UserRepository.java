package com.example.hotel.repository;

import com.example.hotel.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUsername(String username);

    @Query("SELECT u.id FROM users u WHERE LOWER(u.username) = LOWER(:username) and LOWER(u.email = :email)")
    Optional<UUID> findUserIdByUsernameAndEmail(@Param("username") String username,
                                                @Param("email") String email);
}
