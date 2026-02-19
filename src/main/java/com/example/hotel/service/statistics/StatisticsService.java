package com.example.hotel.service.statistics;

import com.example.hotel.entity.statistics.EventType;
import com.example.hotel.entity.statistics.Statistics;
import com.example.hotel.event.BookingEvent;
import com.example.hotel.event.UserRegistrationEvent;
import com.example.hotel.repository.statistics.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    private static final String FIRST_ROW_OF_CSV =
            "id;type;userId;checkIn;checkOut;timestamp\n";

    private static final String ROW_OF_CSV =
            "%s;%s;%s;%s;%s;%s\n";

    public byte[] exportDataToCsv() {
        List<Statistics> data = statisticsRepository.findAll();

        StringBuilder builder = new StringBuilder();

        builder.append(FIRST_ROW_OF_CSV);

        for (Statistics stat : data) {
            builder.append(String.format(ROW_OF_CSV,
                    stat.getId(),
                    stat.getType(),
                    stat.getUserId(),
                    stat.getCheckIn(),
                    stat.getCheckOut(),
                    stat.getCreatedAt()));
        }

        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    public void saveUserRegistrationEvent(UserRegistrationEvent event) {
        Statistics stats = new Statistics();
        stats.setType(EventType.USER_REGISTRATION);
        stats.setUserId(event.userId());
        statisticsRepository.save(stats);
    }

    public void saveBookingEvent(BookingEvent event) {
        Statistics stats = new Statistics();
        stats.setType(EventType.BOOKING);
        stats.setCheckIn(event.checkIn());
        stats.setCheckOut(event.checkOut());
        stats.setUserId(event.userId());
        statisticsRepository.save(stats);
    }
}
