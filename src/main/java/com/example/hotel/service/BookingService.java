package com.example.hotel.service;

import com.example.hotel.entity.Booking;
import com.example.hotel.event.BookingEvent;
import com.example.hotel.event.UserRegistrationEvent;
import com.example.hotel.exception.BookingDatesUnavailableException;
import com.example.hotel.exception.EntityNotFoundException;
import com.example.hotel.repository.BookingRepository;
import com.example.hotel.repository.BookingSpecification;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.security.AppUserPrincipal;
import com.example.hotel.web.dto.v1.BookingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import static com.example.hotel.service.MessageTemplates.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookingService {

    @Value("${app.kafka.kafkaStatisticsService.kafkaBookingCreatedStatus}")
    private String kafkaBookingCreatedStatus;

    private final BookingRepository bookingRepository;

    private final RoomService roomService;

    private final UserService userService;

    private final RoomRepository roomRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Page<Booking> findAll(BookingFilter bookingFilter) {
        Pageable pageable = PageRequest.of(
                bookingFilter.pageNumber(),
                bookingFilter.pageSize()
        );

        return bookingRepository.findAll(
                BookingSpecification.withFilter(bookingFilter),
                pageable
        );
    }

    public Booking findById(UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(MessageFormat.format(TEMPLATE_BOOKING_NOT_FOUND_EXCEPTION, id)));
    }

    @Transactional
    public UUID save(UserDetails userDetails, Booking booking) {

        roomRepository.findByIdWithLock(booking.getRoom().getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(TEMPLATE_ROOM_NOT_FOUND_EXCEPTION));

        Long unavailableCount =
                roomService.countUnavailableDatesByRoomIdAndCheckInDateAndCheckOutDate(
                        booking.getRoom().getId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate()
                );

        if (unavailableCount > 0) {
            throw new BookingDatesUnavailableException(MessageFormat.format(TEMPLATE_BOOKING_DATES_UNAVAILABLE_EXCEPTION,
                    booking.getRoom().getId(),
                    booking.getCheckOutDate(),
                    booking.getCheckInDate()));
        }

        Long overlappingCount =
                bookingRepository.countOverlappingBooking(
                        booking.getRoom().getId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate()
                );

        if (overlappingCount > 0) {
            throw new BookingDatesUnavailableException(MessageFormat.format(TEMPLATE_BOOKING_DATES_UNAVAILABLE_EXCEPTION,
                    booking.getRoom().getId(),
                    booking.getCheckOutDate(),
                    booking.getCheckInDate()));
        }

        AppUserPrincipal passport = (AppUserPrincipal) userDetails;

        booking.setUser(userService.findById(passport.getUserId()));

        Booking savedBooking = bookingRepository.saveAndFlush(booking);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                kafkaTemplate.send(kafkaBookingCreatedStatus, new BookingEvent(
                        savedBooking.getUser().getId(),
                        savedBooking.getCheckInDate(),
                        savedBooking.getCheckOutDate()
                ));
            }
        });

        return savedBooking.getId();
    }
}
