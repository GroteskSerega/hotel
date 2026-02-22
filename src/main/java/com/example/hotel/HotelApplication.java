package com.example.hotel;

import com.example.hotel.entity.*;
import com.example.hotel.entity.statistics.EventType;
import com.example.hotel.entity.statistics.Statistics;
import com.example.hotel.validation.*;
import com.example.hotel.web.dto.ErrorResponse;
import com.example.hotel.web.dto.v1.*;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RegisterReflectionForBinding({
		UserUpsertRequest.class,
		UserFilter.class,
		UserResponse.class,
		UserListResponse.class,

		UserFilterValid.class,
		UserFilterValidValidator.class,

		HotelUpsertRequest.class,
		HotelFilter.class,
		HotelResponse.class,
		HotelListResponse.class,
		HotelRatingUpsertRequest.class,

		HotelFilterValid.class,
		HotelFilterValidValidator.class,

		RoomUpsertRequest.class,
		RoomFilter.class,
		RoomResponse.class,
		RoomListResponse.class,

		RoomFilterValid.class,
		RoomFilterValidValidator.class,

		BookingUpsertRequest.class,
		BookingFilter.class,
		BookingResponse.class,
		BookingListResponse.class,

		BookingFilterValid.class,
		BookingFilterValidValidator.class,

		ErrorResponse.class,

		RoleType.class,
		User.class,
		Hotel.class,
		Room.class,
		Booking.class,

		java.util.ArrayList.class,
		java.util.UUID.class,
		java.util.UUID[].class,
		java.time.Instant.class,
		java.time.Instant[].class

//		EventType.class,
//		Statistics.class
})
@SpringBootApplication
public class HotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelApplication.class, args);
	}

}
