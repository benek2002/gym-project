package com.example.backend.controller;

import com.example.backend.Exception.EntityNotFoundException;
import com.example.backend.Service.UserService;
import com.example.backend.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userDtoList);
    }

    @PostMapping("/{userId}/group/{groupId}")
    public ResponseEntity<Void> addUserToGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        userService.adduserToGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{userId}/group/{groupId}")
    public ResponseEntity<Void> deleteUserFromGroup(@PathVariable Long userId, @PathVariable Long groupId){

        userService.deleteUserFromGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }



}
