package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketSuspension {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    private LocalDateTime issuedAt;

    private LocalDateTime expiredAt;

    @OneToOne
    @JoinColumn(name="gym_ticket_id", referencedColumnName = "id")
    @JsonIgnore
    private GymTicket gymTicket;

    @Transient
    private boolean isExpired;

    @PostLoad
    public void checkIfisExpired(){
       isExpired =  expiredAt.isBefore(LocalDateTime.now());
    }
}
