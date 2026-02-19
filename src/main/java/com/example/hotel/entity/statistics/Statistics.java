package com.example.hotel.entity.statistics;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "statistics")
public class Statistics {
    @Id
    private String id;

    private EventType type;

    private UUID userId;

    private Instant checkIn;

    private Instant checkOut;

    @CreatedDate
    private Instant createdAt;
}
