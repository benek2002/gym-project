package com.example.backend.entity;

import com.example.backend.Utils.DayOfWeek;
import com.example.backend.Utils.GroupName;
import com.example.backend.Utils.GroupType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Entity(name = "training_groups")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime startAt;

    private LocalTime endAt;
    @Enumerated(EnumType.STRING)
    private GroupType groupType;
    @Enumerated(EnumType.STRING)
    private GroupName groupName;

    @ManyToMany(mappedBy = "trainingGroups")
    private Set<User> users;

}
