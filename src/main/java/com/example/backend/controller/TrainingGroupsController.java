package com.example.backend.controller;

import com.example.backend.Service.TrainingGroupService;
import com.example.backend.Utils.GroupType;
import com.example.backend.dto.TrainingGroupDto;
import com.example.backend.entity.TrainingGroup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
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
        if(!trainingGroupDtoList.isEmpty()){
            return new ResponseEntity<>(trainingGroupDtoList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(trainingGroupDtoList, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<TrainingGroupDto> getSingleTrainingGroup(@PathVariable Long groupId){
        TrainingGroupDto trainingGroupDto = trainingGroupService.getSignleGroup(groupId);
        if(trainingGroupDto!=null){
            return new ResponseEntity<>(trainingGroupDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(trainingGroupDto, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/groupType/{groupType}")
    public ResponseEntity<List<TrainingGroupDto>> getTrainingGroupsByGroupType(@PathVariable GroupType groupType){
        List<TrainingGroupDto> trainingGroupDtoList = trainingGroupService.getTrainingGroupsByGroupType(groupType);
        if(!trainingGroupDtoList.isEmpty()){
            return new ResponseEntity<>(trainingGroupDtoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(trainingGroupDtoList, HttpStatus.BAD_REQUEST);
    }


}
