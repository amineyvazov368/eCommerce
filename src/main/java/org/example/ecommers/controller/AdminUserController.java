package org.example.ecommers.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.request.UserRequest;
import org.example.ecommers.dto.request.UserRoleRequest;
import org.example.ecommers.dto.response.UserResponse;
import org.example.ecommers.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
//@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
@RestController
@RequestMapping("/api/admin/users")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
       List<UserResponse> userDto = authService.getAllUsers();
        if (userDto == null) userDto = new ArrayList<>();
       return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse userDto = authService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}/ban")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> banUser(@PathVariable Long id, Authentication authentication) {
        System.out.println("Logged user roles: " + authentication.getAuthorities());
        authService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                  @Valid @RequestBody UserRequest userDto) {
        UserResponse user = authService.updateUser(id, userDto);
        return ResponseEntity.ok(user);
    }


    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole(@PathVariable Long id,
                                                   @Valid @RequestBody UserRoleRequest userDto) {
        UserResponse updatedUser = authService.updateUserRole(id, userDto.getRole());
        return ResponseEntity.ok(updatedUser);
    }
}
