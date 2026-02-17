package com.example.hotel.web.controller.v1;

import com.example.hotel.mapper.HotelMapper;
import com.example.hotel.service.HotelService;
import com.example.hotel.web.dto.v1.HotelFilter;
import com.example.hotel.web.dto.v1.HotelListResponse;
import com.example.hotel.web.dto.v1.HotelResponse;
import com.example.hotel.web.dto.v1.HotelUpsertRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/hotel")
@Controller
public class HotelController {

    private static final String pathToHotelResource = "/api/v1/hotel/{id}";

    private final HotelMapper hotelMapper;

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<HotelListResponse> findAll(@Valid HotelFilter hotelFilter) {
        return ResponseEntity.ok(
                hotelMapper.hotelListToHotelListResponse(
                        hotelService.findAll(hotelFilter)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                hotelMapper.hotelToResponse(
                        hotelService.findById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody HotelUpsertRequest request) {
        return ResponseEntity.created(getUri(
                hotelService.save(hotelMapper.requestToHotel(
                        request
                ))))
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID hotelId,
                                                @RequestBody HotelUpsertRequest request) {
        return ResponseEntity.ok()
                .header("location", getUri(
                        hotelService.update(hotelId, request)
                ).toString())
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID hotelId) {
        hotelService.delete(hotelId);
        return ResponseEntity.noContent()
                .build();
    }

    private URI getUri(UUID id) {
        return UriComponentsBuilder.fromPath(pathToHotelResource)
                .buildAndExpand(id)
                .toUri();
    }
}
