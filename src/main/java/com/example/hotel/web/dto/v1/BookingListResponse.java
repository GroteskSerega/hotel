package com.example.hotel.web.dto.v1;

import java.util.List;

public record BookingListResponse(
        List<BookingResponse> bookings,
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize
) {
}
