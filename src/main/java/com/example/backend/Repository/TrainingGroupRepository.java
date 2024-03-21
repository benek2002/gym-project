package com.example.backend.Repository;

import com.example.backend.entity.TrainingGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingGroupRepository extends JpaRepository<TrainingGroup, Long> {
}
