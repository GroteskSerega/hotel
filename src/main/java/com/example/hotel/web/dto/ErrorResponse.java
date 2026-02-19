package com.example.hotel.web.dto;

import java.time.Instant;

public record ErrorResponse(String errorMessage,
                            Instant timestamp) {
}
