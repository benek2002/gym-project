package com.example.backend.controller;

import com.example.backend.Service.TrainingGroupService;
import com.example.backend.dto.TrainingGroupDto;
import com.example.backend.entity.TrainingGroup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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
}
