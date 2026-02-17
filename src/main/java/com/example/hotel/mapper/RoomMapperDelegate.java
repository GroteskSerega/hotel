package com.example.hotel.mapper;

import com.example.hotel.entity.Room;
import com.example.hotel.service.HotelService;
import com.example.hotel.web.dto.v1.RoomUpsertRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RoomMapperDelegate implements RoomMapper {

    private HotelService hotelService;

    private RoomMapper roomMapper;

    @Autowired
    public void setHotelService(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Autowired
    public void setRoomMapper(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }

    @Override
    public Room requestToRoom(RoomUpsertRequest request) {
        Room room = roomMapper.requestToRoom(request);

        room.setHotel(hotelService.findById(request.hotelId()));

        return room;
    }
}
