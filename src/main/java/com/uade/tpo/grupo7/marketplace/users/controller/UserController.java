package com.uade.tpo.grupo7.marketplace.users.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.grupo7.marketplace.users.dto.CreateAdminRequest;
import com.uade.tpo.grupo7.marketplace.users.dto.UpdateUserRequest;
import com.uade.tpo.grupo7.marketplace.users.dto.UserResponse;
import com.uade.tpo.grupo7.marketplace.users.entity.User;
import com.uade.tpo.grupo7.marketplace.users.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints de usuarios del marketplace")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<UserResponse> getAllUsers(
        @ParameterObject @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable
    ) {
        return this.userService.getAllUsers(pageable);
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("me")
    public UserResponse getMyProfile(
        @AuthenticationPrincipal User user
    ) {
        return this.userService.getUserById(user.getId());
    }

    @PreAuthorize("hasRole('BUYER')")
    @PatchMapping("me")
    public UserResponse updateMyProfile(
        @RequestBody UpdateUserRequest dto,
        @AuthenticationPrincipal User user
    ) {
        return this.userService.updateUserById(user.getId(), dto);
    }

    @PreAuthorize("hasRole('BUYER')")
    @DeleteMapping("me")
    public UserResponse deleteMyProfile(
        @AuthenticationPrincipal User user
    ) {
        this.userService.deleteUserById(user.getId());
        return this.userService.getUserById(user.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{userId}")
    public UserResponse getUserById(@PathVariable Long userId) {
        return this.userService.getUserById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{userId}")
    public UserResponse updateUserById(
        @PathVariable Long userId,
        @RequestBody UpdateUserRequest dto
    ) {
        return this.userService.updateUserById(userId, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        this.userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("admin")
    public UserResponse createAdminUser(@RequestBody CreateAdminRequest dto) {
        return this.userService.createAdminUser(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("{userId}/promote")
    public UserResponse promoteUser(@PathVariable Long userId) {
        return this.userService.promoteUserToSeller(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
        this.userService.deactivateUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
        this.userService.activateUserById(userId);
        return ResponseEntity.noContent().build();
    }

}
