package com.example.backend.dto;

import com.example.backend.Utils.DayOfWeek;
import com.example.backend.Utils.GroupName;
import com.example.backend.Utils.GroupType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingGroupDto {

    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private GroupName groupName;
    @NotNull
    @Enumerated(EnumType.STRING)
    private GroupType groupType;
    @NotNull
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime startAt;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime endAt;

    private Set<UserDto> users;



}
