package com.example.hotel.repository.statistics;

import com.example.hotel.entity.statistics.Statistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends MongoRepository<Statistics, String> {
}
