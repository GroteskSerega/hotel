package com.example.hotel.mapper;

import com.example.hotel.entity.Booking;
import com.example.hotel.web.dto.v1.BookingListResponse;
import com.example.hotel.web.dto.v1.BookingResponse;
import com.example.hotel.web.dto.v1.BookingUpsertRequest;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@DecoratedWith(BookingMapperDelegate.class)
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class, RoomMapper.class})
public interface BookingMapper {

    Booking requestToBooking(BookingUpsertRequest request);

    @Mapping(source = "room", target = "roomResponse")
    @Mapping(source = "user", target = "userResponse")
    BookingResponse bookingToResponse(Booking booking);

    default BookingListResponse bookingListToBookingListResponse(Page<Booking> bookings) {
        return new BookingListResponse(bookings.getContent()
                .stream()
                .map(this::bookingToResponse)
                .toList(),
                bookings.getTotalElements(),
                bookings.getTotalPages(),
                bookings.getNumber(),
                bookings.getSize()
        );
    }
}
