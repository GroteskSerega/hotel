package com.example.hotel.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer capacity;

    @ElementCollection
    @CollectionTable(name = "room_unavailable_dates", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "unavailable_date")
    private List<Instant> unavailableDates = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @ToString.Exclude
    private Hotel hotel;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createAt;

    @UpdateTimestamp
    private Instant updateAt;
}
