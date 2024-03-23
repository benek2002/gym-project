package com.example.backend.Service;

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
        return userList.stream().map(UserMapper::mapUserToUserDto).collect(Collectors.toList());
    }

    @Override
    public String adduserToGroup(Long userId, Long groupId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<TrainingGroup> optionalTrainingGroup = trainingGroupRepository.findById(groupId);
        if(userOptional.isPresent() && optionalTrainingGroup.isPresent()){
            if(!optionalTrainingGroup.get().getUsers().contains(userOptional.get())){
                optionalTrainingGroup.get().addUserToGroup(userOptional.get());
                trainingGroupRepository.save(optionalTrainingGroup.get());
                return "User successfully added to group";
            }else{
                return "User already belongs to this group";
            }

        }
        return "User or group not found";
    }

    @Override
    public String deleteUserFromGroup(Long userId, Long groupId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<TrainingGroup> optionalTrainingGroup = trainingGroupRepository.findById(groupId);
        if(userOptional.isPresent() && optionalTrainingGroup.isPresent()){
            if(optionalTrainingGroup.get().getUsers().contains(userOptional.get())){
                optionalTrainingGroup.get().removeUserFromGroup(userOptional.get());
                trainingGroupRepository.save(optionalTrainingGroup.get());
                return "User successfully removed from group";
            }else{
                return "User doesn't belong to this group";
            }

        }
        return "User or group not found";
    }


}
