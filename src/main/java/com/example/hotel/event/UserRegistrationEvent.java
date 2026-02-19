package com.example.hotel.event;

import java.util.UUID;

public record UserRegistrationEvent(
        UUID userId
) {
}
