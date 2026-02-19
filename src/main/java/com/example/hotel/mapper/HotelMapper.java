package com.example.hotel.mapper;

import com.example.hotel.entity.Hotel;
import com.example.hotel.web.dto.v1.HotelListResponse;
import com.example.hotel.web.dto.v1.HotelResponse;
import com.example.hotel.web.dto.v1.HotelUpsertRequest;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {

    Hotel requestToHotel(HotelUpsertRequest request);

    HotelResponse hotelToResponse(Hotel hotel);

    default HotelListResponse hotelListToHotelListResponse(Page<Hotel> page) {
        return new HotelListResponse(page.getContent()
                .stream()
                .map(this::hotelToResponse)
                .toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    void updateHotel(HotelUpsertRequest request, @MappingTarget Hotel hotel);
}
