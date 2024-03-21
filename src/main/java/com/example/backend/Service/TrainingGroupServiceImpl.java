package com.example.backend.Service;

import com.example.backend.Repository.TrainingGroupRepository;
import com.example.backend.Repository.UserRepository;
import com.example.backend.dto.TrainingGroupDto;
import com.example.backend.entity.TrainingGroup;
import com.example.backend.entity.User;
import com.example.backend.mappers.TrainingGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
                .users(new HashSet<User>())
                .build();
        TrainingGroup savedTrainingGroup = trainingGroupRepository.save(trainingGroup);


        return TrainingGroupMapper.mapTrainingGroupToTrainingGroupDto(savedTrainingGroup);
    }
}
