package com.example.backend.RepositoryTests;

import com.example.backend.Repository.TrainingGroupRepository;
import com.example.backend.Utils.GroupType;
import com.example.backend.entity.TrainingGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TrainingGroupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TrainingGroupRepository trainingGroupRepository;

    @Test
    @Transactional
    public void testFindAllByGroupType(){
        TrainingGroup trainingGroup1 = new TrainingGroup();
        trainingGroup1.setGroupType(GroupType.MARTIAL_ARTS);
        entityManager.persist(trainingGroup1);

        TrainingGroup trainingGroup2 = new TrainingGroup();
        trainingGroup2.setGroupType(GroupType.MARTIAL_ARTS);
        entityManager.persist(trainingGroup2);

        TrainingGroup trainingGroup3 = new TrainingGroup();
        trainingGroup3.setGroupType(GroupType.DANCE);
        entityManager.persist(trainingGroup3);
        entityManager.flush();

        List<TrainingGroup> trainingGroupList = trainingGroupRepository.findAllByGroupType(GroupType.MARTIAL_ARTS);
        assertEquals(2, trainingGroupList.size());
        assertEquals(GroupType.MARTIAL_ARTS, trainingGroupList.get(0).getGroupType());

    }
}
