package org.example.ecommers.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.UserDto;
import org.example.ecommers.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
       List<UserDto> userDto = authService.getAllUsers();
       return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto userDto = authService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UserDto userDto) {
        UserDto user = authService.updateUser(id, userDto);
        return ResponseEntity.ok(user);
    }
}
