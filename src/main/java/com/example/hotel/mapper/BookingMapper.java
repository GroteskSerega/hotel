package com.example.hotel.mapper;

import com.example.hotel.entity.Booking;
import com.example.hotel.web.dto.v1.BookingListResponse;
import com.example.hotel.web.dto.v1.BookingResponse;
import com.example.hotel.web.dto.v1.BookingUpsertRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@DecoratedWith(BookingMapperDelegate.class)
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class, RoomMapper.class})
public interface BookingMapper {

    Booking requestToBooking(BookingUpsertRequest request);

    BookingResponse bookingToResponse(Booking booking);

    default BookingListResponse bookingListToBookingListResponse(List<Booking> bookingList) {
        return new BookingListResponse(bookingList
                .stream()
                .map(this::bookingToResponse)
                .toList());
    }
}
