package com.example.hotel.service.statistics;

import com.example.hotel.entity.statistics.EventType;
import com.example.hotel.entity.statistics.Statistics;
import com.example.hotel.event.BookingEvent;
import com.example.hotel.event.UserRegistrationEvent;
import com.example.hotel.repository.statistics.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    private static final String FIRST_ROW_OF_CSV =
            "id;type;userId;checkIn;checkOut;createAt\n";

    private static final char DELIMITER_CSV = ';';

    public Resource exportDataToCsvResource() {
        final List<Statistics> data = statisticsRepository.findAll();

        final StringBuilder builder = new StringBuilder(data.size() * 64);

        builder.append(FIRST_ROW_OF_CSV);

        for (final Statistics stat : data) {
            builder.append(stat.getId()).append(DELIMITER_CSV)
                    .append(stat.getType()).append(DELIMITER_CSV)
                    .append(stat.getUserId()).append(DELIMITER_CSV)
                    .append(stat.getCheckIn()).append(DELIMITER_CSV)
                    .append(stat.getCheckOut()).append(DELIMITER_CSV)
                    .append(stat.getCreatedAt()).append('\n');
        }

        byte[] bytes = builder.toString().getBytes(StandardCharsets.UTF_8);
        return new ByteArrayResource(bytes);
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
