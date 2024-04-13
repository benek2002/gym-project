package com.example.backend.controllerTests;

import com.example.backend.AuthenticationController;
import com.example.backend.Service.AuthenticationService;
import com.example.backend.dto.AuthenticationResponse;
import com.example.backend.dto.LoginDto;
import com.example.backend.dto.RegisterDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {AuthenticationController.class})
@WebMvcTest
@WithMockUser
class AuthenticationControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private Gson gson;
    @Test
    void registerTest() throws Exception {
        RegisterDto registerDto = RegisterDto.builder()
                .firstName("test")
                .lastName("test")
                .email("test")
                .password("test")
                .build();
        when(authenticationService.register(registerDto)).thenReturn(AuthenticationResponse.builder().token("1234").build());

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(registerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("1234"));

    }

    @Test
    void loginTest() throws Exception {
        LoginDto loginDto = LoginDto.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        when(authenticationService.login(loginDto)).thenReturn(AuthenticationResponse.builder()
                        .token("test")
                .build());
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test"));
    }
}