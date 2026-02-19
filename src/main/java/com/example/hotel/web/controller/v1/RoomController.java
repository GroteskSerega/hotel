package com.example.hotel.web.controller.v1;

import com.example.hotel.mapper.RoomMapper;
import com.example.hotel.service.RoomService;
import com.example.hotel.web.dto.v1.RoomFilter;
import com.example.hotel.web.dto.v1.RoomListResponse;
import com.example.hotel.web.dto.v1.RoomResponse;
import com.example.hotel.web.dto.v1.RoomUpsertRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
@RestController
public class RoomController {

    private static final String pathToRoomResource = "/api/v1/room/{id}";

    private final RoomMapper roomMapper;

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<RoomListResponse> findAll(@Valid RoomFilter roomFilter) {
        return ResponseEntity.ok(
                roomMapper.roomListToRoomListResponse(
                        roomService.findAll(roomFilter)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                roomMapper.roomToResponse(
                        roomService.findById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid RoomUpsertRequest request) {
        return ResponseEntity.created(getUri(
                roomService.save(roomMapper.requestToRoom(request))
        )).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID roomId,
                                       @RequestBody @Valid RoomUpsertRequest request) {
        return ResponseEntity.ok()
                .location(getUri(
                        roomService.update(roomId, request)
                ))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID roomId) {
        roomService.delete(roomId);
        return ResponseEntity.noContent()
                .build();
    }

    private URI getUri(UUID id) {
        return UriComponentsBuilder.fromPath(pathToRoomResource)
                .buildAndExpand(id)
                .toUri();
    }
}
