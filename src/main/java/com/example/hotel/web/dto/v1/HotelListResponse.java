package com.example.hotel.web.dto.v1;

import java.util.List;

public record HotelListResponse(
        List<HotelResponse> hotels,
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize
) {
}
