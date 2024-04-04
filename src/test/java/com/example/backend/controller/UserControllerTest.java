package com.example.backend.controller;

import com.example.backend.Service.UserService;
import com.example.backend.Utils.Role;
import com.example.backend.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {UserController.class})
@WebMvcTest
@WithMockUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;


    @Test
    void shouldGetAllUsers() throws Exception {
        List<UserDto> mockedUsers = List.of(
                UserDto.builder()
                        .firstName("Janek")
                        .lastName("Pozycz")
                        .email("janek@gmail.com")
                        .role(Role.USER)
                        .id(0L)
                        .build(),
                UserDto.builder()
                        .firstName("Janek2")
                        .lastName("Pozycz2")
                        .email("janek2@gmail.com")
                        .role(Role.USER)
                        .id(1L)
                        .build()
        );


        when(userService.getAllUsers()).thenReturn(mockedUsers);
        List<UserDto> result = userService.getAllUsers();
        System.out.println(result);

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    void shouldAddUserToGroup() throws Exception {

        Long userId = 1L;
        Long groupId = 1L;

        doNothing().when(userService).addUserToGroup(userId, groupId);

        mockMvc.perform(post("/users/{userId}/group/{groupId}", userId, groupId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).addUserToGroup(userId, groupId);
    }

    @Test
    void shouldDeleteUserFromGroup() throws Exception {

        Long userId = 1L;
        Long groupId = 1L;

        mockMvc.perform(delete("/users/{userId}/group/{groupId}", userId, groupId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUserFromGroup(userId, groupId);
    }
}