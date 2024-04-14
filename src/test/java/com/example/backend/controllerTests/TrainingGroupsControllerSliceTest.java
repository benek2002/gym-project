package com.example.backend.controllerTests;

import com.example.backend.Service.TrainingGroupService;
import com.example.backend.Utils.DayOfWeek;
import com.example.backend.Utils.GroupName;
import com.example.backend.controller.TrainingGroupsController;
import com.example.backend.dto.TrainingGroupDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static com.example.backend.Utils.GroupType.MARTIAL_ARTS;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {TrainingGroupsController.class})
@WebMvcTest
@WithMockUser
class TrainingGroupsControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainingGroupService trainingGroupService;

    @Test
    public void shouldAddTrainingGroup() throws Exception {
        LocalTime startAt = LocalTime.of(15, 30);
        LocalTime endAt = LocalTime.of(17,0);

        TrainingGroupDto trainingGroupDto = TrainingGroupDto.builder()
                .groupName(GroupName.BOXING)
                .groupType(MARTIAL_ARTS)
                .description("Test Description")
                .dayOfWeek(DayOfWeek.MONDAY)
                .startAt(startAt)
                .endAt(endAt)
                .build();
        TrainingGroupDto trainingGroupDtoFromService = TrainingGroupDto.builder()
                .id(0L)
                .groupName(GroupName.BOXING)
                .groupType(MARTIAL_ARTS)
                .description("Test1 Description")
                .dayOfWeek(DayOfWeek.MONDAY)
                .startAt(startAt)
                .endAt(endAt)
                .build();
        when(trainingGroupService.addTrainingGroup(trainingGroupDto)).thenReturn(trainingGroupDtoFromService);
        mockMvc.perform(post("/training-group").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingGroupDto)))
                .andExpect(status().isCreated());


    }
    @Test
    public void shouldGetAllTrainingGroups() throws Exception {
        List<TrainingGroupDto> trainingGroupDtoList = List.of(TrainingGroupDto.builder()
                        .id(0L)
                .build(), TrainingGroupDto.builder()
                        .id(1L)
                .build());
        when(trainingGroupService.getAllTrainingGroups()).thenReturn(trainingGroupDtoList);
        mockMvc.perform(get("/training-group")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void shouldGetSingleTrainingGroup() throws Exception {
        Long groupId = 0L;
        TrainingGroupDto trainingGroupDto = TrainingGroupDto.builder()
                .id(0L)
                .build();
        when(trainingGroupService.getSingleGroup(groupId)).thenReturn(trainingGroupDto);
        mockMvc.perform(get("/training-group/{groupId}", groupId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(groupId));
    }

    @Test
    public void shouldGetTrainingGroupsByGroupType() throws Exception {
        List<TrainingGroupDto> trainingGroupDtoList = List.of(TrainingGroupDto.builder()
                .id(0L)
                .groupType(MARTIAL_ARTS)
                .build(), TrainingGroupDto.builder()
                .id(1L)
                .groupType(MARTIAL_ARTS)
                .build());

        when(trainingGroupService.getTrainingGroupsByGroupType(MARTIAL_ARTS)).thenReturn(trainingGroupDtoList);
        mockMvc.perform(get("/training-group/groupType/{groupType}", MARTIAL_ARTS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].groupType").value("MARTIAL_ARTS"));


    }


}