package com.example.hotel.web.dto;

import java.time.LocalDateTime;

public record ErrorResponse(String errorMessage,
                            LocalDateTime timestamp) {
}
