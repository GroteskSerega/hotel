package com.example.hotel.service;

import com.example.hotel.entity.Hotel;
import com.example.hotel.exception.EntityNotFoundException;
import com.example.hotel.mapper.HotelMapper;
import com.example.hotel.repository.HotelRepository;
import com.example.hotel.repository.HotelSpecification;
import com.example.hotel.web.dto.v1.HotelFilter;
import com.example.hotel.web.dto.v1.HotelUpsertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import static com.example.hotel.service.MessageTemplates.TEMPLATE_HOTEL_NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    private final HotelMapper hotelMapper;

    public List<Hotel> findAll(HotelFilter hotelFilter) {
        return hotelRepository.fetchAll(
                HotelSpecification.withFilter(hotelFilter),
                PageRequest.of(
                        hotelFilter.pageNumber(),
                        hotelFilter.pageSize()
                )
        ).getContent();
    }

    public Hotel findById(UUID id) {
        return hotelRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(MessageFormat.format(TEMPLATE_HOTEL_NOT_FOUND_EXCEPTION, id)));
    }

    @Transactional
    public UUID save(Hotel hotel) {
        return hotelRepository.saveAndFlush(hotel).getId();
    }

    @Transactional
    public UUID update(UUID hotelId, HotelUpsertRequest request) {
        Hotel existedHotel = findById(hotelId);

        hotelMapper.updateHotel(request, existedHotel);

        return hotelRepository.save(existedHotel).getId();
    }

    @Transactional
    public void delete(UUID id) {
        findById(id);

        hotelRepository.deleteById(id);
    }

    @Transactional
    public UUID updateRating(UUID id, Integer newMark) {
        Hotel existedHotel = hotelRepository.findByIdWithLock(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(MessageFormat.format(TEMPLATE_HOTEL_NOT_FOUND_EXCEPTION, id)));

        double currentRating =
                existedHotel.getRating() != null ? existedHotel.getRating() : 0.0;
        int currentRatingCount =
                existedHotel.getRatingCount() != null ? existedHotel.getRatingCount() : 0;

        double totalRating =
                currentRating * currentRatingCount;

        totalRating =
                totalRating - currentRating + newMark;

        double rating = 0;

        if (currentRatingCount > 0) {
            rating = totalRating / currentRatingCount;
            rating = Math.round(rating * 10.0) / 10.0;
        } else {
            rating = totalRating;
        }

        existedHotel.setRating(rating);
        existedHotel.setRatingCount(currentRatingCount + 1);

        return hotelRepository.saveAndFlush(existedHotel).getId();
    }
}
