package com.example.backend.dto;

import com.example.backend.Utils.DayOfWeek;
import com.example.backend.Utils.GroupName;
import com.example.backend.Utils.GroupType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingGroupDto {

    private Long id;
    @NotNull
    private GroupName groupName;
    @NotNull
    private GroupType groupType;
    @NotNull
    private String description;
    @NotNull
    private DayOfWeek dayOfWeek;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startAt;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endAt;

    private Set<UserDto> userList;

}
