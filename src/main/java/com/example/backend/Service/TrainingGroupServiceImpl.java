package com.example.backend.Service;

import com.example.backend.Repository.TrainingGroupRepository;
import com.example.backend.Repository.UserRepository;
import com.example.backend.Utils.GroupType;
import com.example.backend.dto.TrainingGroupDto;
import com.example.backend.dto.UserDto;
import com.example.backend.entity.TrainingGroup;
import com.example.backend.entity.User;
import com.example.backend.mappers.TrainingGroupMapper;
import com.example.backend.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingGroupServiceImpl implements TrainingGroupService {

    private final TrainingGroupRepository trainingGroupRepository;
    private final UserRepository userRepository;
    @Override
    public TrainingGroupDto addTrainingGroup(TrainingGroupDto trainingGroupDto) {
        TrainingGroup trainingGroup = TrainingGroup.builder()
                .groupName(trainingGroupDto.getGroupName())
                .groupType(trainingGroupDto.getGroupType())
                .startAt(trainingGroupDto.getStartAt())
                .endAt(trainingGroupDto.getEndAt())
                .description(trainingGroupDto.getDescription())
                .dayOfWeek(trainingGroupDto.getDayOfWeek())
                .build();
        TrainingGroup savedTrainingGroup = trainingGroupRepository.save(trainingGroup);


        return TrainingGroupMapper.mapTrainingGroupToTrainingGroupDto(savedTrainingGroup);
    }

    @Override
    public List<TrainingGroupDto> getAllTrainingGroups() {
        return trainingGroupRepository.findAll().stream().map(TrainingGroupMapper::mapTrainingGroupToTrainingGroupDto).collect(Collectors.toList());
    }

    @Override
    public TrainingGroupDto getSignleGroup(Long groupId) {
        Optional<TrainingGroup> optionalTrainingGroup =  trainingGroupRepository.findById(groupId);
        if(optionalTrainingGroup.isPresent()){
            TrainingGroupDto trainingGroupDto = TrainingGroupMapper.mapTrainingGroupToTrainingGroupDto(optionalTrainingGroup.get());
            Set<UserDto> userDtoList = userRepository.findUsersByTrainingGroupId(trainingGroupDto.getId()).stream().map(UserMapper::mapUserToUserDto).collect(Collectors.toSet());
            trainingGroupDto.setUsers(userDtoList);
            return trainingGroupDto;

        }
        return null;
    }

    @Override
    public List<TrainingGroupDto> getTrainingGroupsByGroupType(GroupType groupType) {
        return trainingGroupRepository.findAllByGroupType(groupType).stream().map(TrainingGroupMapper::mapTrainingGroupToTrainingGroupDto).collect(Collectors.toList());
    }
}
