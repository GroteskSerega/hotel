package com.example.hotel.web.controller.v1.statistics;

import com.example.hotel.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadCsv() {
        byte[] data = statisticsService.exportDataToCsv();
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statistics.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }
}
