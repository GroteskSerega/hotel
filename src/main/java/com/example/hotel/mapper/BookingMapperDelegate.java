package com.example.hotel.mapper;

import com.example.hotel.entity.Booking;
import com.example.hotel.service.RoomService;
import com.example.hotel.web.dto.v1.BookingUpsertRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BookingMapperDelegate implements BookingMapper {

    private BookingMapper bookingMapper;

    private RoomService roomService;

    @Autowired
    public void setBookingMapper(BookingMapper bookingMapper) {
        this.bookingMapper = bookingMapper;
    }

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public Booking requestToBooking(BookingUpsertRequest request){
        Booking booking = bookingMapper.requestToBooking(request);

        booking.setRoom(roomService.findById(request.roomId()));

        return booking;
    }
}
