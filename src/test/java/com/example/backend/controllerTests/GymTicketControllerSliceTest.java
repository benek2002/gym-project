package com.example.backend.controllerTests;

import com.example.backend.Service.TicketService;
import com.example.backend.Utils.TicketType;
import com.example.backend.controller.GymTicketController;
import com.example.backend.dto.TicketDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextConfiguration(classes = {GymTicketController.class})
@WebMvcTest
@WithMockUser
class GymTicketControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TicketService ticketService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldBuyTicket() throws Exception {
        Long userId = 0L;
        when(ticketService.addTicket(userId, TicketType.ONE_MONTH)).thenReturn(TicketDto.builder()
                        .id(0L)
                        .user_id(userId)
                        .issuedAt(LocalDateTime.now())
                        .ticketType(TicketType.ONE_MONTH)
                        .expiredAt(LocalDateTime.now().plusMonths(1))
                .build());
        mockMvc.perform(post("/tickets/{userId}", userId)
                        .param("ticketType", String.valueOf(TicketType.ONE_MONTH))
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(userId));
    }

    @Test
    void shouldBuyTicketInAdvance() throws Exception {
        Long userId = 0L;
        LocalDateTime date = LocalDateTime.now().plusMonths(1);
        when(ticketService.addTicket(userId, TicketType.ONE_MONTH)).thenReturn(TicketDto.builder()
                .id(0L)
                .user_id(userId)
                .issuedAt(date)
                .ticketType(TicketType.ONE_MONTH)
                .expiredAt(date.plusMonths(1))
                .build());
        mockMvc.perform(post("/tickets/{userId}", userId)
                        .param("ticketType", String.valueOf(TicketType.ONE_MONTH))
                        .param("date", String.valueOf(date))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(userId));

    }

    @Test
    void shouldGetAllUserTickets() throws Exception {
        Long userId = 0L;
        List<TicketDto> ticketDtoList = List.of(TicketDto.builder()
                        .id(0L)
                        .user_id(userId)
                        .build(),
                TicketDto.builder()
                        .id(1L)
                        .user_id(userId)
                        .build());
        when(ticketService.getAllUserTickets(userId)).thenReturn(ticketDtoList);
        mockMvc.perform(get("/tickets/all/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldGetSingleTicket() throws Exception {
        Long ticketId = 0L;
        TicketDto ticketDtoFromService = TicketDto.builder()
                .id(0L)
                .build();
        when(ticketService.getSingleTicket(ticketId)).thenReturn(ticketDtoFromService);

        mockMvc.perform(get("/tickets/{ticketId}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId));
    }

    @Test
    void shouldHangYourTicket() throws Exception {
        Long userId = 0L;
        doNothing().when(ticketService).hangYourTicket(userId);
        mockMvc.perform(put("/tickets/user/hangTicket/{userId}", userId).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetCurrentUserTicket() throws Exception {
        Long userId = 0L;
        TicketDto ticketDtoFromService = TicketDto.builder()
                .user_id(userId)
                .build();
        when(ticketService.getCurrentUserTicket(userId)).thenReturn(ticketDtoFromService);
        String expectedResponseBody = objectMapper.writeValueAsString(ticketDtoFromService);
        mockMvc.perform(get("/tickets/current/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponseBody));
    }
}