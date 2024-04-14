package com.example.backend.controllerTests;

import com.example.backend.Repository.UserRepository;
import com.example.backend.Utils.Role;
import com.example.backend.config.JwtService;
import com.example.backend.config.SecurityConfiguration;
import com.example.backend.controller.AuthenticationController;
import com.example.backend.dto.LoginDto;
import com.example.backend.dto.RegisterDto;
import com.example.backend.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    @Transactional
    public void shouldRegister() throws Exception {
        RegisterDto registerDto = RegisterDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("doe@gmail.com")
                .password("test")
                .build();
                List<User> usersBefore = userRepository.findAll();
                mockMvc.perform(post("/api/auth/register").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated());
                List<User> usersAfter = userRepository.findAll();
                assertTrue(usersAfter.size()>usersBefore.size());

    }

    @Test
    @Transactional
    public void shouldLogin() throws Exception {
        User user = User.builder()
                .firstName("test")
                .lastName("test")
                .email("test@gmail.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("test"))
                .build();
        userRepository.save(user);
        LoginDto loginDto = LoginDto.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        String token = JsonPath.read(content, "$.token");

        String username = jwtService.extractUsername(token);
        assertEquals(loginDto.getEmail(), username);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        assertTrue(jwtService.isTokenValid(token, userDetails));

        mockMvc.perform(get("/api/auth/secured")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/auth/secured"))
                .andExpect(status().is(403));
        mockMvc.perform(get("/api/auth/secured")
                        .header("Authorization", "Bearer1 " + token))
                .andExpect(status().is(403));

    }


}
