package com.example.backend.controllerTests.serviceTests;

import com.example.backend.Exception.EntityNotFoundException;
import com.example.backend.Repository.TrainingGroupRepository;
import com.example.backend.Repository.UserRepository;
import com.example.backend.Service.TrainingGroupServiceImpl;
import com.example.backend.Utils.DayOfWeek;
import com.example.backend.Utils.GroupName;
import com.example.backend.Utils.GroupType;
import com.example.backend.Utils.Role;
import com.example.backend.dto.TrainingGroupDto;
import com.example.backend.entity.TrainingGroup;
import com.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingGroupServiceTest {

    @Mock
    private TrainingGroupRepository trainingGroupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TrainingGroupServiceImpl trainingGroupService;

    @Test
    public void shouldAddTrainingGroup(){
        TrainingGroupDto trainingGroupDto = TrainingGroupDto.builder()
                .groupName(GroupName.BOXING)
                .groupType(GroupType.MARTIAL_ARTS)
                .description("test")
                .dayOfWeek(DayOfWeek.MONDAY)
                .startAt(LocalTime.of(15, 30))
                .endAt(LocalTime.of(17, 0))
                .build();
        TrainingGroup trainingGroup = TrainingGroup.builder()
                .id(0L)
                .groupName(GroupName.BOXING)
                .groupType(GroupType.MARTIAL_ARTS)
                .description("test")
                .dayOfWeek(DayOfWeek.MONDAY)
                .startAt(LocalTime.of(15, 30))
                .endAt(LocalTime.of(17, 0))
                .build();

        when(trainingGroupRepository.save(Mockito.any(TrainingGroup.class))).thenReturn(trainingGroup);
        TrainingGroupDto trainingGroupDtoFromservice = trainingGroupService.addTrainingGroup(trainingGroupDto);
        assertEquals(trainingGroupDto.getDescription(),trainingGroupDtoFromservice.getDescription());
    }

    @Test
    public void shouldGetAllTrainingGroups(){
        TrainingGroup trainingGroup = TrainingGroup.builder()
                .id(0L)
                .groupName(GroupName.BOXING)
                .groupType(GroupType.MARTIAL_ARTS)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startAt(LocalTime.of(15,30))
                .endAt(LocalTime.of(17, 0))
                .description("test")
                .build();
        List<TrainingGroup> trainingGroupList = Collections.singletonList(trainingGroup);
        when(trainingGroupRepository.findAll()).thenReturn(trainingGroupList);
        List<TrainingGroupDto> trainingGroupDtoList = trainingGroupService.getAllTrainingGroups();
        assertEquals(trainingGroupDtoList.size(), 1);
        assertEquals(trainingGroupDtoList.stream().findFirst().get().getId(), trainingGroup.getId());
    }

    @Test
    public void shouldGetSingleGroup(){
        Long groupId = 0L;
        TrainingGroup trainingGroup = TrainingGroup.builder()
                .id(0L)
                .groupName(GroupName.BOXING)
                .groupType(GroupType.MARTIAL_ARTS)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startAt(LocalTime.of(15,30))
                .endAt(LocalTime.of(17, 0))
                .description("test")
                .build();
        User user = User.builder()
                .role(Role.USER)
                .email("test")
                .id(0L)
                .password("test")
                .lastName("test")
                .firstName("test")
                .trainingGroups(Set.of(trainingGroup))
                .build();
        when(trainingGroupRepository.findById(groupId)).thenReturn(Optional.of(trainingGroup));
        when(userRepository.findUsersByTrainingGroupId(groupId)).thenReturn(Collections.singletonList(user));
        TrainingGroupDto trainingGroupDtoFromService = trainingGroupService.getSingleGroup(groupId);
        assertEquals("test", trainingGroupDtoFromService.getDescription());
        assertEquals(user.getId(), trainingGroupDtoFromService.getUsers().stream().findAny().get().getId());

    }

    @Test
    public void getSingleGroup_WhenTrainingGroupDoesNotExist_ShouldThrowEntityNotFoundException(){
        Long groupId = 0L;
        when(trainingGroupRepository.findById(groupId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> trainingGroupService.getSingleGroup(groupId));
    }

    @Test
    public void shouldGetTrainingGroupsByGroupType(){
        GroupType groupType = GroupType.MARTIAL_ARTS;
        TrainingGroup trainingGroup1= TrainingGroup.builder()
                .id(0L)
                .groupName(GroupName.BOXING)
                .groupType(groupType)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startAt(LocalTime.of(15,30))
                .endAt(LocalTime.of(17, 0))
                .description("test")
                .build();
        TrainingGroup trainingGroup2 = TrainingGroup.builder()
                .id(0L)
                .groupName(GroupName.KICK_BOXING)
                .groupType(groupType)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startAt(LocalTime.of(15,30))
                .endAt(LocalTime.of(17, 0))
                .description("test")
                .build();
        List<TrainingGroup> trainingGroupList = List.of(trainingGroup1, trainingGroup2);
        when(trainingGroupRepository.findAllByGroupType(groupType)).thenReturn(trainingGroupList);
        List<TrainingGroupDto> trainingGroupDtoList = trainingGroupService.getTrainingGroupsByGroupType(groupType);
        assertEquals(2, trainingGroupDtoList.size());
        assertEquals(trainingGroup1.getId(), trainingGroupDtoList.stream().findFirst().get().getId());

    }




}
