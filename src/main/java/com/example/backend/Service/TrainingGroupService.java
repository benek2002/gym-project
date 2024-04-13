package com.example.backend.Service;

import com.example.backend.Utils.GroupType;
import com.example.backend.dto.TrainingGroupDto;

import java.util.List;

public interface TrainingGroupService {
    TrainingGroupDto addTrainingGroup(TrainingGroupDto trainingGroupDto);

    List<TrainingGroupDto> getAllTrainingGroups();

    TrainingGroupDto getSingleGroup(Long groupId);

    List<TrainingGroupDto> getTrainingGroupsByGroupType(GroupType groupType);
}
