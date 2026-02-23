package com.example.hotel.service;

import com.example.hotel.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class WarmupService {

    private final RestClient.Builder restClientBuilder;

    private final UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    private void onApplicationReady() throws JsonProcessingException {
        log.info("Starting warmup for Postgres and MongoDB...");

        try {
            RestClient client = restClientBuilder.baseUrl("http://localhost:8080").build();

            client.get()
                    .uri("/actuator/health")
                    .retrieve()
                    .toBodilessEntity();

            userRepository.count();
        } catch (Exception e) {
            log.error("Warmup error. Message: " + e.getMessage());
        }

        log.info("Warmup completed.");
    }

//    @EventListener(ApplicationReadyEvent.class)
//    private void onApplicationReady() throws JsonProcessingException {
//        log.info("Starting warmup for Postgres and MongoDB...");
//
//        User warmupUser = createUserWarmup();
//
//        RestClient client = restClientBuilder.baseUrl("http://localhost:8080").build();
//
//        client.get()
//                .uri("/api/v1/user?pageSize=1&pageNumber=0")
//                .header("Authorization",
//                        "Basic " + Base64.getEncoder().encodeToString(
//                                (warmupUser.getUsername() + ":" + warmupUser.getUsername()
//                                ).getBytes()))
//                .retrieve()
//                .toBodilessEntity();
//
//        deleteUserWarmup(warmupUser.getId());
//
//        log.info("Warmup completed.");
//    }
//
//    private User createUserWarmup() {
//        String warmupUsername = UUID.randomUUID().toString();
//
//        String email = warmupUsername + "@" + warmupUsername + ".ru";
//
//        User warmupUser = new User();
//        warmupUser.setUsername(warmupUsername);
//        warmupUser.setPassword(passwordEncoder.encode(warmupUsername));
//        warmupUser.setRoles(new ArrayList<>(List.of(RoleType.ROLE_ADMIN)));
//
//        return userRepository.save(warmupUser);
//    }
//
//    private void deleteUserWarmup(UUID id) {
//        userRepository.deleteById(id);
//    }
}
