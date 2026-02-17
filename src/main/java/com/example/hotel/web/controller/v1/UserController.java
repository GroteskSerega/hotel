package com.example.hotel.web.controller.v1;


import com.example.hotel.entity.Role;
import com.example.hotel.entity.RoleType;
import com.example.hotel.mapper.UserMapper;
import com.example.hotel.service.UserService;
import com.example.hotel.web.dto.v1.UserFilter;
import com.example.hotel.web.dto.v1.UserListResponse;
import com.example.hotel.web.dto.v1.UserResponse;
import com.example.hotel.web.dto.v1.UserUpsertRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private static final String pathToUserResource = "/api/v1/user/{id}";

    private final UserMapper userMapper;

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserListResponse> findAll(@Valid UserFilter userFilter) {
        return ResponseEntity.ok(
                userMapper.userListToUserListResponse(
                        userService.findAll(userFilter)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                userMapper.userToResponse(userService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid UserUpsertRequest request,
                                               @RequestParam RoleType roleType) {
        return ResponseEntity.created(getUri(
                userService.save(
                        userMapper.requestToUser(request),
                        Role.from(roleType)
                )
        )).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID userId,
                                       @RequestBody @Valid UserUpsertRequest request,
                                       @RequestParam RoleType roleType) {
        return ResponseEntity.ok()
                .header("location", getUri(
                        userService.update(userId,
                                request,
                                Role.from(roleType))
                        ).toString())
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    private URI getUri(UUID id) {
        return UriComponentsBuilder.fromPath(pathToUserResource)
                .buildAndExpand(id)
                .toUri();
    }
}
