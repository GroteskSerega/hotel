package com.example.hotel.listener.statistics;

import com.example.hotel.event.BookingEvent;
import com.example.hotel.event.UserRegistrationEvent;
import com.example.hotel.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StatisticsListener {

    private final StatisticsService statisticsService;

    @KafkaListener(topics = "${app.kafka.kafkaStatisticsService.kafkaUserCreatedStatus}",
            groupId = "${app.kafka.kafkaMessageGroupId}")
    public void listenUserRegistration(UserRegistrationEvent event) {
        statisticsService.saveUserRegistrationEvent(event);
    }

    @KafkaListener(topics = "${app.kafka.kafkaStatisticsService.kafkaBookingCreatedStatus}",
            groupId = "${app.kafka.kafkaMessageGroupId}")
    public void listenBookingEvent(BookingEvent event) {
        statisticsService.saveBookingEvent(event);
    }
}
