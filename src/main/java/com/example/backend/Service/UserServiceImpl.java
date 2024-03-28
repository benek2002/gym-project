package com.example.backend.Service;

import com.example.backend.Exception.EntityNotFoundException;
import com.example.backend.Repository.TrainingGroupRepository;
import com.example.backend.Repository.UserRepository;
import com.example.backend.dto.UserDto;
import com.example.backend.entity.TrainingGroup;
import com.example.backend.entity.User;
import com.example.backend.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final TrainingGroupRepository trainingGroupRepository;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList =  userRepository.findAll();
        return userList.stream().map(UserMapper::mapUserToUserDto).toList();
    }

    @Override
    public void adduserToGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
        TrainingGroup trainingGroup = trainingGroupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException(TrainingGroup.class, groupId));

            if(!trainingGroup.getUsers().contains(user)){
                trainingGroup.addUserToGroup(user);
                trainingGroupRepository.save(trainingGroup);

            }else{
                throw new RuntimeException("User already belongs to this training group");
            }



    }

    @Override
    public void deleteUserFromGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
        TrainingGroup trainingGroup = trainingGroupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException(TrainingGroup.class, groupId));

            if(trainingGroup.getUsers().contains(user)) {
                trainingGroup.removeUserFromGroup(user);
                trainingGroupRepository.save(trainingGroup);
            }else{
                throw new RuntimeException("User doesn't belong to this training group");
            }


    }


}
