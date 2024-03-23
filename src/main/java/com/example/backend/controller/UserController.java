package com.example.backend.controller;

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
        if(!userDtoList.isEmpty()){
            return ResponseEntity.ok(userDtoList);
        }
        return new ResponseEntity<>(userDtoList, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{userId}/group/{groupId}")
    public ResponseEntity<String> addUserToGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        String response = userService.adduserToGroup(userId, groupId);
        if(response == "User successfully added to group") {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("/{userId}/group/{groupId}")
    public ResponseEntity<String> deleteUserFromGroup(@PathVariable Long userId, @PathVariable Long groupId){

        String response = userService.deleteUserFromGroup(userId, groupId);
        if(Objects.equals(response, "User successfully removed from group")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }



}
