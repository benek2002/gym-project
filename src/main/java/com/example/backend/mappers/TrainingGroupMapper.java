package com.example.backend.mappers;

import com.example.backend.dto.TrainingGroupDto;
import com.example.backend.dto.UserDto;
import com.example.backend.entity.TrainingGroup;

import java.util.stream.Collectors;

public class TrainingGroupMapper {

    public static TrainingGroupDto mapTrainingGroupToTrainingGroupDto(TrainingGroup trainingGroup){
        return TrainingGroupDto.builder()
                        .id(trainingGroup.getId())
                .description(trainingGroup.getDescription())
                .userList(trainingGroup.getUsers().stream().map(UserMapper::mapUserToUserDto).collect(Collectors.toSet()))
                .dayOfWeek(trainingGroup.getDayOfWeek())
                .startAt(trainingGroup.getStartAt())
                .endAt(trainingGroup.getEndAt())
                .groupType(trainingGroup.getGroupType())
                .groupName(trainingGroup.getGroupName())
                .build();



    }
}
