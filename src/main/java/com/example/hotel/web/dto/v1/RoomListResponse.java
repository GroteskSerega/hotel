package com.example.hotel.web.dto.v1;

import java.util.List;

public record RoomListResponse(
        List<RoomResponse> rooms,
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize
) {
}
