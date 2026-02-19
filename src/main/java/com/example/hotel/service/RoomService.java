package com.example.hotel.service;

import com.example.hotel.entity.Room;
import com.example.hotel.exception.EntityNotFoundException;
import com.example.hotel.mapper.RoomMapper;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.repository.RoomSpecification;
import com.example.hotel.web.dto.v1.RoomFilter;
import com.example.hotel.web.dto.v1.RoomUpsertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.example.hotel.service.MessageTemplates.TEMPLATE_ROOM_NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    public List<Room> findAll(RoomFilter roomFilter) {
        return roomRepository.fetchAll(
                RoomSpecification.withFilter(roomFilter),
                PageRequest.of(
                        roomFilter.pageNumber(),
                        roomFilter.pageSize()
                )
        ).getContent();
    }

    public Room findById(UUID id) {
        return roomRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(MessageFormat.format(TEMPLATE_ROOM_NOT_FOUND_EXCEPTION, id)));
    }

    @Transactional
    public UUID save(Room room) {
        return roomRepository.saveAndFlush(room).getId();
    }

    @Transactional
    public UUID update(UUID roomId, RoomUpsertRequest request) {
        Room existedRoom = findById(roomId);

        roomMapper.updateRoom(request, existedRoom);

        return roomRepository.save(existedRoom).getId();
    }

    @Transactional
    public void delete(UUID id) {
        findById(id);

        roomRepository.deleteById(id);
    }


    public Long countUnavailableDatesByRoomIdAndCheckInDateAndCheckOutDate(UUID roomId, Instant checkInDate, Instant checkOutDate) {
        return roomRepository.countUnavailableDatesByRoomId(roomId, checkInDate, checkOutDate);
    }
}
