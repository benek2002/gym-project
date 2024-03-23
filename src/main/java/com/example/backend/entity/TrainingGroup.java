package com.example.backend.entity;

import com.example.backend.Utils.DayOfWeek;
import com.example.backend.Utils.GroupName;
import com.example.backend.Utils.GroupType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "training_groups")
@Builder
@Getter
@Setter
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
    @JsonIgnore
    private Set<User> users = new HashSet<>();


    public void addUserToGroup(User user){
        users.add(user);
        user.getTrainingGroups().add(this);

    }

    public void removeUserFromGroup(User user) {
        users.remove(user);
        user.getTrainingGroups().remove(this);
    }
}
