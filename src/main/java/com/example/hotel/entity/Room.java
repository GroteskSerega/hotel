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

    private String name;

    private String description;

    @Column(name = "room_number")
    private String roomNumber;

    private BigDecimal price;

    private Integer capacity;

    @Column(name = "unavailable_dates")
//    @ElementCollection
    private List<Instant> unavailableDates = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @ToString.Exclude
    private Hotel hotel;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createAt;

    @UpdateTimestamp
    private Instant updateAt;
}
