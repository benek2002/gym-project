package com.example.backend.controllerTests.serviceTests;

import com.example.backend.Exception.BackendException;
import com.example.backend.Exception.EntityNotFoundException;
import com.example.backend.Repository.TrainingGroupRepository;
import com.example.backend.Repository.UserRepository;
import com.example.backend.Service.UserServiceImpl;
import com.example.backend.dto.UserDto;
import com.example.backend.entity.TrainingGroup;
import com.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainingGroupRepository trainingGroupRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldGetAllUsers(){
        List<User> users = List.of(User.builder()
                        .id(0L)
                .build(), User.builder()
                        .id(1L)
                .build());
        when(userRepository.findAll()).thenReturn(users);
        List<UserDto> result = userService.getAllUsers();
        assertEquals(users.size(), result.size());

    }

    @Test
    public void shouldAddUserToGroup(){
        Long userId = 0L;
        Long groupId=0L;
        User user = new User();
        TrainingGroup trainingGroup = new TrainingGroup();


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(trainingGroupRepository.findById(groupId)).thenReturn(Optional.of(trainingGroup));

        userService.addUserToGroup(userId, groupId);

        verify(trainingGroupRepository, times(1)).save(trainingGroup);
        assertTrue(trainingGroup.getUsers().contains(user));
    }

    @Test
    public void addUserToGroup_WhenUserDoesNotExist_ShouldThrowEntityNotFoundException(){
        Long userId = 0L;
        Long groupId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.addUserToGroup(userId, groupId));
    }

    @Test
    public void addUserToGroup_WhenTrainingGroupDoesNotExist_ShouldThrowEntityNotFoundException(){
        Long userId = 0L;
        Long groupId = 0L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(trainingGroupRepository.findById(groupId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.addUserToGroup(userId, groupId));
    }

    @Test
    public void addUserToGroup_WhenUserAlreadyBelongsToGroup_ShouldThrowBackendException(){
        Long userId = 0L;
        Long trainingGroupId = 0L;
        User user = new User();
        TrainingGroup trainingGroup = new TrainingGroup();
        Set<User> users = new HashSet<>();
        users.add(user);
        trainingGroup.setUsers(users);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(trainingGroupRepository.findById(trainingGroupId)).thenReturn(Optional.of(trainingGroup));
        assertThrows(BackendException.class, () -> userService.addUserToGroup(userId, trainingGroupId));
    }

    @Test
    public void shouldDeleteUserFromGroup(){
        Long userId = 0L;
        Long groupId=0L;
        User user = new User();
        TrainingGroup trainingGroup = new TrainingGroup();
        Set<User> users = new HashSet<>();
        users.add(user);
        trainingGroup.setUsers(users);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(trainingGroupRepository.findById(groupId)).thenReturn(Optional.of(trainingGroup));

        userService.deleteUserFromGroup(userId, groupId);
        verify(trainingGroupRepository, times(1)).save(trainingGroup);
        assertFalse(trainingGroup.getUsers().contains(user));

    }

    @Test
    public void deleteUserFromGroup_WhenUserDoesNotExist_ShouldThrowEntityNotFoundException(){
        Long userId = 0L;
        Long groupId = 0L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserFromGroup(userId, groupId));
    }

    @Test
    public void deleteUserFromGroup_WhenTrainingGroupDoesNotExist_ShouldThrowEntityNotFoundException(){
        Long userId = 0L;
        Long groupId = 0L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(trainingGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserFromGroup(userId, groupId));
    }

    @Test
    public void deleteUserFromGroup_WhenUserDoesNotBelongToGroup_ShouldThrowBackendException(){
        Long userId = 0L;
        Long groupId=0L;
        User user = new User();
        TrainingGroup trainingGroup = new TrainingGroup();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(trainingGroupRepository.findById(groupId)).thenReturn(Optional.of(trainingGroup));


        assertThrows(BackendException.class, () -> userService.deleteUserFromGroup(userId, groupId));

    }

}
