package com.example.backend;

import com.example.backend.Service.TicketService;
import com.example.backend.Utils.TicketType;
import com.example.backend.dto.TicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class GymTicketController {

    private final TicketService ticketService;

    @PostMapping("/{userId}")
    public ResponseEntity<TicketDto> buyTicket(@PathVariable Long userId, @RequestParam TicketType ticketType){

            TicketDto ticketDto = ticketService.addTicket(userId, ticketType);
            return ResponseEntity.ok().body(ticketDto);

    }

    @PostMapping("/buyTicketInAdvance/{userId}")  // The date must be provide with time for example: date=2024-06-19T14:30:00
    public ResponseEntity<TicketDto> buyTicketInAdvance(@PathVariable Long userId, @RequestParam TicketType ticketType, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime){
        TicketDto ticketDto = ticketService.addTicket(userId, ticketType, dateTime);
        return ResponseEntity.ok().body(ticketDto);

    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<TicketDto>> getAllUserTickets(@PathVariable Long userId){
        List<TicketDto> ticketDtoList = ticketService.getAllUserTickets(userId);
        return ResponseEntity.ok(ticketDtoList);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDto> getSingleTicket(@PathVariable Long ticketId) {
        TicketDto ticketDto = ticketService.getSingleTicket(ticketId);
        return ResponseEntity.ok(ticketDto);
    }

    @PutMapping("/user/hangTicket/{userId}")
    public ResponseEntity<String> hangYourTicket(@PathVariable Long userId){
        ticketService.hangYourTicket(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current/{userId}")
    public ResponseEntity<TicketDto> getCurrentUserTicket(@PathVariable Long userId){
        TicketDto ticketDto = ticketService.getCurrentUserTicket(userId);
        return ResponseEntity.ok(ticketDto);
    }


}
