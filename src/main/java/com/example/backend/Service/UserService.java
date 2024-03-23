package com.example.backend.Service;

import com.example.backend.dto.UserDto;

import java.util.List;

public interface UserService {


    List<UserDto> getAllUsers();

    String adduserToGroup(Long userId, Long groupId);

    String deleteUserFromGroup(Long userId, Long groupId);
}
