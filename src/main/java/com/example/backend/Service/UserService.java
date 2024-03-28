package com.example.backend.Service;

import com.example.backend.dto.UserDto;

import java.util.List;

public interface UserService {


    List<UserDto> getAllUsers();

    void adduserToGroup(Long userId, Long groupId);

    void deleteUserFromGroup(Long userId, Long groupId);
}
