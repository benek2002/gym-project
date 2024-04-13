package com.example.backend.controllerTests.serviceTests;

import com.example.backend.Repository.UserRepository;
import com.example.backend.Service.AuthenticationService;
import com.example.backend.Utils.Role;
import com.example.backend.config.JwtService;
import com.example.backend.dto.AuthenticationResponse;
import com.example.backend.dto.LoginDto;
import com.example.backend.dto.RegisterDto;
import com.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void testRegister(){
        RegisterDto registerDto = RegisterDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .password("password")
                .build();

        User user = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password("encodedPassword")
                .role(Role.USER)
                .build();
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(jwtService.generateToken(Mockito.any(User.class))).thenReturn("jwtToken");
        AuthenticationResponse response = authenticationService.register(registerDto);
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    public void testLogin(){
        LoginDto loginDto = LoginDto.builder()
                .email("john@gmail.com")
                .password("test")
                .build();
        User user = User.builder()
                .id(0L)
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(Mockito.any(User.class))).thenReturn("jwtToken");
        AuthenticationResponse response = authenticationService.login(loginDto);
        assertEquals("jwtToken", response.getToken());
        assertEquals("0", response.getUserId());
    }
}
