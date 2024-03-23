package com.example.backend.mappers;

import com.example.backend.dto.UserDto;
import com.example.backend.entity.User;

public class UserMapper {

    public static UserDto mapUserToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
