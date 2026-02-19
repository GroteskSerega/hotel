package com.example.hotel.web.controller.v1;

import com.example.hotel.mapper.BookingMapper;
import com.example.hotel.service.BookingService;
import com.example.hotel.web.dto.v1.BookingFilter;
import com.example.hotel.web.dto.v1.BookingListResponse;
import com.example.hotel.web.dto.v1.BookingResponse;
import com.example.hotel.web.dto.v1.BookingUpsertRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
@RestController
public class BookingController {

    private static final String pathToBookingResource = "/api/v1/booking/{id}";

    private final BookingMapper bookingMapper;

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<BookingListResponse> findAll(@Valid BookingFilter bookingFilter) {
        return ResponseEntity.ok(
                bookingMapper.bookingListToBookingListResponse(
                        bookingService.findAll(bookingFilter)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                bookingMapper.bookingToResponse(
                        bookingService.findById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<Void> create(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestBody @Valid BookingUpsertRequest request) {
        return ResponseEntity.created(getUri(
                bookingService.save(userDetails,
                        bookingMapper.requestToBooking(request))
        )).build();
    }

    private URI getUri(UUID id) {
        return UriComponentsBuilder.fromPath(pathToBookingResource)
                .buildAndExpand(id)
                .toUri();
    }
}
