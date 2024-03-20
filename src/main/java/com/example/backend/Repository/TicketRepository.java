package com.example.backend.Repository;

import com.example.backend.entity.GymTicket;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<GymTicket, Long> {
    List<GymTicket> findByExpirationAtBefore(LocalDateTime now);

    Optional<GymTicket> findByUser(User user);

    List<GymTicket> findAllByUser(User user);

    List<GymTicket> findAllByUserAndIssuedAtBefore(User user, LocalDateTime now);

    List<GymTicket> findAllByUserAndIssuedAtAfter(User user, LocalDateTime now);

}
