package com.example.backend.RepositoryTests;

import com.example.backend.Repository.TicketRepository;
import com.example.backend.controller.TrainingGroupsController;
import com.example.backend.entity.GymTicket;
import com.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TicketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;
    @Test
    @Transactional
    public void testFindAllByUser(){
        User user = new User();
        entityManager.persist(user);
        GymTicket gymTicket1 = new GymTicket();
        GymTicket gymTicket2 = new GymTicket();
        gymTicket1.setUser(user);
        gymTicket2.setUser(user);
        entityManager.persist(gymTicket1);
        entityManager.persist(gymTicket2);
        entityManager.flush();
        List<GymTicket> gymTicketList = ticketRepository.findAllByUser(user);
        assertEquals(2, gymTicketList.size());

    }

    @Test
    @Transactional
    public void testFindAllByUserAndIssuedAtBefore(){
        LocalDateTime localDateTimeBefore = LocalDateTime.now().minusDays(1);
        LocalDateTime localDateTimeAfter = LocalDateTime.now().plusDays(1);
        User user = new User();
        entityManager.persist(user);
        GymTicket gymTicket1 = new GymTicket();
        gymTicket1.setIssuedAt(localDateTimeBefore);
        GymTicket gymTicket2 = new GymTicket();
        gymTicket2.setIssuedAt(localDateTimeAfter);
        gymTicket1.setUser(user);
        gymTicket2.setUser(user);
        entityManager.persist(gymTicket1);
        entityManager.persist(gymTicket2);
        entityManager.flush();
        List<GymTicket> gymTicketList = ticketRepository.findAllByUserAndIssuedAtBefore(user, LocalDateTime.now());
        assertEquals(1, gymTicketList.size());
        assertEquals(localDateTimeBefore, gymTicketList.get(0).getIssuedAt());
    }

    @Test
    @Transactional
    public void testFindAllByUserAndIssuedAtAf(){
        LocalDateTime localDateTimeBefore = LocalDateTime.now().minusDays(1);
        LocalDateTime localDateTimeAfter = LocalDateTime.now().plusDays(1);
        User user = new User();
        entityManager.persist(user);
        GymTicket gymTicket1 = new GymTicket();
        gymTicket1.setIssuedAt(localDateTimeBefore);
        GymTicket gymTicket2 = new GymTicket();
        gymTicket2.setIssuedAt(localDateTimeAfter);
        gymTicket1.setUser(user);
        gymTicket2.setUser(user);
        entityManager.persist(gymTicket1);
        entityManager.persist(gymTicket2);
        entityManager.flush();
        List<GymTicket> gymTicketList = ticketRepository.findAllByUserAndIssuedAtAfter(user, LocalDateTime.now());
        assertEquals(1, gymTicketList.size());
        assertEquals(localDateTimeAfter, gymTicketList.get(0).getIssuedAt());
    }
}
