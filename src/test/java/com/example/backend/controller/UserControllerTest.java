package com.example.backend.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class UserControllerTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;
    @Value("${sql.script.create.user}")
    private String sqlAddUser;
    @Value("${sql.script.create.training_group}")
    private String sqlAddTrainingGroup;
    @Value("${sql.script.create.ticket}")
    private String sqlAddTicket;
    @Value("${sql.script.create.ticket_suspension}")
    private String sqlCreateTicketSuspension;
    @Value("${sql.script.add.user_training_group}")
    private String sqlAddUserToTrainingGroup;
    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;
    @Value("${sql.script.delete.training_group}")
    private String sqlDeleteTrainingGroup;
    @Value("${sql.script.delete.user_training_group}")
    private String sqlDeleteUserFromTrainingGroup;
    @Value("${sql.script.delete.ticket}")
    private String sqlDeleteTicket;
    @Value("${sql.script.delete.ticket_suspension}")
    private String sqlDeleteTicketSuspension;

    @BeforeEach
    public void setupDatabase(){
        jdbc.execute(sqlAddTicket);
        jdbc.execute(sqlAddUser);
        jdbc.execute(sqlAddTrainingGroup);

        jdbc.execute(sqlCreateTicketSuspension);
        jdbc.execute(sqlAddUserToTrainingGroup);
    }

    @AfterEach
    public void clearDatabase(){
        jdbc.execute(sqlDeleteTicketSuspension);
        jdbc.execute(sqlDeleteTicket);
        jdbc.execute(sqlDeleteUserFromTrainingGroup);
        jdbc.execute(sqlDeleteTrainingGroup);
        jdbc.execute(sqlDeleteUser);
    }
    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }
}