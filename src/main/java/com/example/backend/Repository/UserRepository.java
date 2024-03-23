package com.example.backend.Repository;


import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    @Query("Select u From _user u JOIN u.trainingGroups tg WHERE tg.id = :groupId")
    List<User> findUsersByTrainingGroupId(@Param("groupId") Long groupId);
}
