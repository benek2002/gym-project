package com.example.backend.controller;

import com.example.Utils.LocalDateTypeAdapter.LocalDateTypeAdapter;
import com.example.backend.Service.TrainingGroupService;
import com.example.backend.Utils.DayOfWeek;
import com.example.backend.Utils.GroupName;
import com.example.backend.dto.TrainingGroupDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static com.example.backend.Utils.GroupType.MARTIAL_ARTS;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
            .create();

    @Test
    public void shouldAddTrainingGroup() throws Exception {
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusHours(1);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;


        LocalDateTime parsedStartAt = LocalTime.parse(startAt.toLocalTime().format(formatter), formatter).atDate(startAt.toLocalDate());
        LocalDateTime parsedEndAt = LocalTime.parse(endAt.toLocalTime().format(formatter), formatter).atDate(endAt.toLocalDate());

        TrainingGroupDto trainingGroupDto = TrainingGroupDto.builder()
                .groupName(GroupName.BOXING)
                .groupType(MARTIAL_ARTS)
                .description("Test Description")
                .dayOfWeek(DayOfWeek.MONDAY)
                .startAt(parsedStartAt)
                .endAt(parsedEndAt)
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
        when(trainingGroupService.getSignleGroup(groupId)).thenReturn(trainingGroupDto);
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