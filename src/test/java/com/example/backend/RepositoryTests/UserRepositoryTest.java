package com.example.backend.RepositoryTests;

import com.example.backend.Repository.UserRepository;
import com.example.backend.entity.TrainingGroup;
import com.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("test@gmail.com");
        entityManager.persist(user);
        entityManager.flush();
        Optional<User> userFromRepository = userRepository.findByEmail("test@gmail.com");
        assertTrue(userFromRepository.isPresent());
        assertEquals(user.getEmail(), userFromRepository.get().getEmail());
    }

    @Test
    @Transactional
    public void testFindUsersByTrainingGroupsId() {
        TrainingGroup trainingGroup = new TrainingGroup();
        entityManager.persist(trainingGroup);
        User user1 = new User();
        user1.setEmail("user1@gmail.com");
        user1.getTrainingGroups().add(trainingGroup);
        entityManager.persist(user1);
        User user2 = new User();
        user2.setEmail("user2@gmail.com");
        entityManager.persist(user2);
        entityManager.flush();

        List<User> userInGroup = userRepository.findUsersByTrainingGroupId(trainingGroup.getId());
        assertEquals(1, userInGroup.size());
        assertEquals(user1.getEmail(), userInGroup.get(0).getEmail());


    }
}