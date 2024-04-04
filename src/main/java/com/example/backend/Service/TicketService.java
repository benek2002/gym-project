package com.example.backend.Service;


import com.example.backend.Utils.TicketType;
import com.example.backend.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketService {

    TicketDto addTicket(Long userId, TicketType ticketType);
    TicketDto addTicket(Long userId, TicketType ticketType, LocalDateTime dateTime);

    List<TicketDto> getAllUserTickets(Long userId);

    TicketDto getSingleTicket(Long ticketId);

    void hangYourTicket(Long userId);

    TicketDto getCurrentUserTicket(Long userId);

}
