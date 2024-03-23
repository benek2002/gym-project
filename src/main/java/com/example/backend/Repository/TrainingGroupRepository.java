package com.example.backend.Repository;

import com.example.backend.Utils.GroupType;
import com.example.backend.dto.TrainingGroupDto;
import com.example.backend.entity.TrainingGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingGroupRepository extends JpaRepository<TrainingGroup, Long> {
    List<TrainingGroup> findAllByGroupType(GroupType groupType);
}
