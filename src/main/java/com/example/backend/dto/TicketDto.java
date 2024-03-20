package com.example.backend.dto;

import com.example.backend.Utils.TicketType;
import com.example.backend.entity.TicketSuspension;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {

    private Long id;

    private Long user_id;

    private TicketType ticketType;

    private LocalDateTime issuedAt;

    private LocalDateTime expiredAt;

    private TicketSuspension ticketSuspension;

}
