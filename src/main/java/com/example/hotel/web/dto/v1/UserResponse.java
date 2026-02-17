package com.example.hotel.web.dto.v1;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        Instant createAt,
        Instant updateAt
) {
}
