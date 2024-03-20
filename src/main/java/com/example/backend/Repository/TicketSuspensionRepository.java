package com.example.backend.Repository;

import com.example.backend.entity.TicketSuspension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketSuspensionRepository extends JpaRepository<TicketSuspension, Long> {

    List<TicketSuspension> findAllByExpiredAtBefore(LocalDateTime now);

}
