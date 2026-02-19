package com.example.hotel.mapper;

import com.example.hotel.entity.Room;
import com.example.hotel.web.dto.v1.RoomListResponse;
import com.example.hotel.web.dto.v1.RoomResponse;
import com.example.hotel.web.dto.v1.RoomUpsertRequest;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@DecoratedWith(RoomMapperDelegate.class)
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    Room requestToRoom(RoomUpsertRequest request);

    RoomResponse roomToResponse(Room room);

    default RoomListResponse roomListToRoomListResponse(Page<Room> rooms) {
        return new RoomListResponse(rooms.getContent()
                .stream()
                .map(this::roomToResponse)
                .toList(),
                rooms.getTotalElements(),
                rooms.getTotalPages(),
                rooms.getNumber(),
                rooms.getSize()
        );
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    void updateRoom(RoomUpsertRequest request, @MappingTarget Room room);
}
