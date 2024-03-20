package com.example.backend.mappers;

import com.example.backend.dto.TicketDto;
import com.example.backend.entity.GymTicket;

public class TicketMapper {

    public static TicketDto mapGymTickewtToTicketDTo(GymTicket gymTicket){
        return TicketDto.builder()
                .id(gymTicket.getId())
                .ticketType(gymTicket.getTicketType())
                .user_id(gymTicket.getUser().getId())
                .expiredAt(gymTicket.getExpirationAt())
                .issuedAt(gymTicket.getIssuedAt())
                .ticketSuspension(gymTicket.getSuspension())
                .build();
    }
}
