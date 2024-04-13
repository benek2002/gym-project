package com.example.backend.Repository;

import com.example.backend.entity.GymTicket;
import com.example.backend.entity.TicketSuspension;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface TicketRepository extends JpaRepository<GymTicket, Long> {

    List<GymTicket> findAllByUser(User user);

    List<GymTicket> findAllByUserAndIssuedAtBefore(User user, LocalDateTime now);

    List<GymTicket> findAllByUserAndIssuedAtAfter(User user, LocalDateTime now);


}
