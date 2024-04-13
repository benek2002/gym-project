package com.example.backend;

import com.example.backend.Service.TrainingGroupService;
import com.example.backend.Utils.GroupType;
import com.example.backend.dto.TrainingGroupDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/training-group")
public class TrainingGroupsController {
    private final TrainingGroupService trainingGroupService;

    @PostMapping
    public ResponseEntity<TrainingGroupDto> addTrainingGroup(@Valid @RequestBody TrainingGroupDto trainingGroupDto){
        TrainingGroupDto trainingGroupDtoFromService = trainingGroupService.addTrainingGroup(trainingGroupDto);
        return new ResponseEntity<>(trainingGroupDtoFromService, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TrainingGroupDto>> getAllTrainingGroups(){
        List<TrainingGroupDto> trainingGroupDtoList = trainingGroupService.getAllTrainingGroups();
        return ResponseEntity.ok(trainingGroupDtoList);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<TrainingGroupDto> getSingleTrainingGroup(@PathVariable Long groupId){
        TrainingGroupDto trainingGroupDto = trainingGroupService.getSingleGroup(groupId);
        return ResponseEntity.ok(trainingGroupDto);
    }

    @GetMapping("/groupType/{groupType}")
    public ResponseEntity<List<TrainingGroupDto>> getTrainingGroupsByGroupType(@PathVariable GroupType groupType) {
        List<TrainingGroupDto> trainingGroupDtoList = trainingGroupService.getTrainingGroupsByGroupType(groupType);
        return ResponseEntity.ok(trainingGroupDtoList);

    }
}
