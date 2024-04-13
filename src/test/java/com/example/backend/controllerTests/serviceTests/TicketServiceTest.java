package com.example.backend.controllerTests.serviceTests;

import com.example.backend.Exception.EntityNotFoundException;
import com.example.backend.Exception.HangTicketException;
import com.example.backend.Exception.TicketPurchaseNotValidException;
import com.example.backend.Repository.TicketRepository;
import com.example.backend.Repository.TicketSuspensionRepository;
import com.example.backend.Repository.UserRepository;
import com.example.backend.Service.TicketServiceImpl;
import com.example.backend.Utils.TicketType;
import com.example.backend.dto.TicketDto;
import com.example.backend.entity.GymTicket;
import com.example.backend.entity.TicketSuspension;
import com.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketSuspensionRepository ticketSuspensionRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    public void shouldAddTicket() {
        Long userId = 0L;
        User user = new User();
        TicketType ticketType = TicketType.ONE_MONTH;
        user.setId(userId);
        GymTicket gymTicket = new GymTicket();
        gymTicket.setId(0L);
        gymTicket.setUser(user);
        gymTicket.setTicketType(ticketType);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUser(user)).thenReturn(Collections.emptyList());
        when(ticketRepository.save(Mockito.any(GymTicket.class))).thenReturn(gymTicket);

        TicketDto ticketDto = ticketService.addTicket(userId, ticketType);
        assertNotNull(ticketDto);
        assertEquals(ticketType, ticketDto.getTicketType());
    }

    @Test
    public void addTicket_whenAlreadyPurchasedTicket_thenThrowsTicketPurchaseNotValidException() {
        Long userId = 0L;
        TicketType ticketType = TicketType.ONE_MONTH;
        LocalDateTime issuedAt = LocalDateTime.of(2024, 4, 13, 10, 0);
        LocalDateTime expirationAt = issuedAt.plusMonths(1);
        User user = new User();
        user.setId(userId);
        List<GymTicket> gymTicketList = List.of(
                GymTicket.builder().issuedAt(issuedAt).expirationAt(expirationAt).build()
        );
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUser(user)).thenReturn(gymTicketList);

        TicketPurchaseNotValidException exception = assertThrows(TicketPurchaseNotValidException.class, () -> ticketService.addTicket(userId, ticketType));
        assertEquals("You currently have a ticket purchased", exception.getMessage());
    }

    @Test
    public void addTicket_whenAlreadyPurchasedTicketInFuture_thenThrowsTicketPurchaseNotValidException() {
        Long userId = 0L;
        TicketType ticketType = TicketType.ONE_MONTH;
        LocalDateTime issuedAt = LocalDateTime.now().plusWeeks(1);
        LocalDateTime expirationAt = issuedAt.plusMonths(1);
        User user = new User();
        user.setId(userId);
        List<GymTicket> gymTicketList = List.of(
                GymTicket.builder().issuedAt(issuedAt).expirationAt(expirationAt).build()
        );
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUser(user)).thenReturn(gymTicketList);

        TicketPurchaseNotValidException exception = assertThrows(TicketPurchaseNotValidException.class, () -> ticketService.addTicket(userId, ticketType));
        assertEquals("you already have a ticket that starts within 1 month", exception.getMessage());
    }

    @Test
    public void addTicket_WhenUserDoesNotExist_ThenThrowsEntityNotFoundException() {
        Long userId = 0L;
        TicketType ticketType = TicketType.ONE_MONTH;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> ticketService.addTicket(userId, ticketType));
    }

    @Test
    public void shouldAddTicketInFuture() {
        Long userId = 0L;
        User user = new User();
        TicketType ticketType = TicketType.ONE_MONTH;
        user.setId(userId);
        GymTicket gymTicket = new GymTicket();
        gymTicket.setId(0L);
        gymTicket.setUser(user);
        gymTicket.setTicketType(ticketType);
        LocalDateTime plannedIssuedAt = LocalDateTime.now().plusWeeks(1);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUser(user)).thenReturn(Collections.emptyList());
        when(ticketRepository.save(Mockito.any(GymTicket.class))).thenReturn(gymTicket);

        TicketDto ticketDto = ticketService.addTicket(userId, ticketType, plannedIssuedAt);
        assertNotNull(ticketDto);
        assertEquals(ticketType, ticketDto.getTicketType());
    }


    @Test
    public void addTicketInFuture_whenAlreadyPurchasedTicket_thenThrowsTicketPurchaseNotValidException() {
        Long userId = 0L;
        TicketType ticketType = TicketType.ONE_MONTH;
        LocalDateTime plannedIssuedAt = LocalDateTime.of(2024, 4, 17, 10, 0);
        LocalDateTime issuedAt = LocalDateTime.of(2024, 4, 13, 10, 0);
        LocalDateTime expirationAt = issuedAt.plusMonths(1);
        User user = new User();
        user.setId(userId);
        List<GymTicket> gymTicketList = List.of(
                GymTicket.builder().issuedAt(issuedAt).expirationAt(expirationAt).build()
        );
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUser(user)).thenReturn(gymTicketList);

        TicketPurchaseNotValidException exception = assertThrows(TicketPurchaseNotValidException.class, () -> ticketService.addTicket(userId, ticketType, plannedIssuedAt));
        assertEquals("You currently have a ticket purchased", exception.getMessage());
    }

    @Test
    public void addTicketInFuture_whenAlreadyPurchasedTicketInFuture_thenThrowsTicketPurchaseNotValidException() {
        Long userId = 0L;
        TicketType ticketType = TicketType.ONE_MONTH;
        LocalDateTime plannedIssuedAt = LocalDateTime.of(2024, 4, 17, 10, 0);
        LocalDateTime issuedAt = LocalDateTime.of(2024, 4, 27, 10, 0);
        LocalDateTime expirationAt = issuedAt.plusMonths(1);
        User user = new User();
        user.setId(userId);
        List<GymTicket> gymTicketList = List.of(
                GymTicket.builder().issuedAt(issuedAt).expirationAt(expirationAt).build()
        );
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUser(user)).thenReturn(gymTicketList);

        TicketPurchaseNotValidException exception = assertThrows(TicketPurchaseNotValidException.class, () -> ticketService.addTicket(userId, ticketType, plannedIssuedAt));
        assertEquals("you already have a ticket that starts within 1 month of your intended purchase date", exception.getMessage());
    }

    @Test
    public void shouldGetAllUserTickets() {
        Long userId = 0L;
        User user = new User();
        user.setId(userId);
        GymTicket gymTicket = new GymTicket();
        gymTicket.setId(0L);
        gymTicket.setUser(user);
        List<GymTicket> gymTicketList = List.of(gymTicket);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUser(user)).thenReturn(gymTicketList);

        List<TicketDto> ticketDtoList = ticketService.getAllUserTickets(userId);
        assertEquals(gymTicketList.size(), ticketDtoList.size());
        assertEquals(gymTicketList.get(0).getUser().getId(), ticketDtoList.get(0).getUser_id());

    }

    @Test
    public void getAllUserTickets_WhenUserDoesNotHaveTickets_shouldReturnEmptyList() {
        Long userId = 0L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUser(user)).thenReturn(Collections.emptyList());
        List<TicketDto> ticketDtoList = ticketService.getAllUserTickets(userId);
        assertTrue(ticketDtoList.isEmpty());
    }

    @Test
    public void getAllUserTickets_WhenUserDoesNotExist_shouldThrowEntityNotFoundException() {
        Long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> ticketService.getAllUserTickets(userId));
    }

    @Test
    public void shouldGetSingleTicket() {
        Long ticketId = 0L;
        GymTicket gymTicket = new GymTicket();
        gymTicket.setId(0L);
        User user = new User();
        Long userId = 0L;
        user.setId(userId);
        gymTicket.setUser(user);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(gymTicket));
        TicketDto ticketDto = ticketService.getSingleTicket(ticketId);
        assertEquals(gymTicket.getId(), ticketDto.getId());
    }

    @Test
    public void getSingleTicket_WhenTicketDoesNotExist_ThenThrowEnitiyNotFoundException() {
        Long ticketId = 0L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> ticketService.getSingleTicket(ticketId));
    }

    @Test
    public void shouldHangYourTicket() {
        Long userId = 0L;
        LocalDateTime issuedAt = LocalDateTime.now().minusDays(1);
        LocalDateTime expirationAt = issuedAt.plusMonths(1);
        User user = new User();
        user.setId(userId);
        GymTicket gymTicket = new GymTicket();
        gymTicket.setUser(user);
        gymTicket.setIssuedAt(issuedAt);
        gymTicket.setExpirationAt(expirationAt);
        List<GymTicket> gymTicketList = List.of(gymTicket);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUserAndIssuedAtBefore(Mockito.any(User.class), Mockito.any(LocalDateTime.class))).thenReturn(gymTicketList);
        when(ticketRepository.findAllByUserAndIssuedAtAfter(Mockito.any(User.class), Mockito.any(LocalDateTime.class))).thenReturn(Collections.emptyList());
        ticketService.hangYourTicket(userId);
        verify(ticketSuspensionRepository, times(1)).save(Mockito.any(TicketSuspension.class));
        verify(ticketRepository, times(1)).save(Mockito.any(GymTicket.class));
    }

    @Test
    public void hangYourTicket_WhenUserHaveTicketInThisTimePeriod_ShouldThrowHangTicketException() {
        Long userId = 0L;
        LocalDateTime issuedAt = LocalDateTime.now().minusDays(1);
        LocalDateTime expirationAt = issuedAt.plusMonths(1);
        LocalDateTime futureIssuedAt = expirationAt.plusDays(2);
        LocalDateTime futureExpirationAt = futureIssuedAt.plusMonths(1);
        User user = new User();
        user.setId(userId);
        GymTicket gymTicket = new GymTicket();
        gymTicket.setUser(user);
        gymTicket.setIssuedAt(issuedAt);
        gymTicket.setExpirationAt(expirationAt);
        List<GymTicket> gymTicketList = List.of(gymTicket);
        GymTicket futureGymTicket = new GymTicket();
        futureGymTicket.setUser(user);
        futureGymTicket.setIssuedAt(futureIssuedAt);
        futureGymTicket.setExpirationAt(futureExpirationAt);
        List<GymTicket> futureGymTickets = List.of(futureGymTicket);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUserAndIssuedAtBefore(Mockito.any(User.class), Mockito.any(LocalDateTime.class))).thenReturn(gymTicketList);
        when(ticketRepository.findAllByUserAndIssuedAtAfter(Mockito.any(User.class), Mockito.any(LocalDateTime.class))).thenReturn(futureGymTickets);

        HangTicketException exception = assertThrows(HangTicketException.class, () -> ticketService.hangYourTicket(userId));
        assertEquals("You have another ticket in this time period", exception.getMessage());
    }

    @Test
    public void hangYourTicket_WhenUserDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> ticketService.hangYourTicket(userId));
    }

    @Test
    public void shouldGetCurrentUserTicket() {
        Long userId = 0L;
        LocalDateTime issuedAt = LocalDateTime.now().minusDays(5);
        LocalDateTime expirationAt = issuedAt.plusMonths(1);
        User user = new User();
        user.setId(userId);
        GymTicket ticket = new GymTicket();
        ticket.setExpirationAt(expirationAt);
        ticket.setIssuedAt(issuedAt);
        ticket.setUser(user);
        List<GymTicket> gymTicketList = List.of(ticket);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUserAndIssuedAtBefore(Mockito.any(User.class), Mockito.any(LocalDateTime.class))).thenReturn(gymTicketList);
        TicketDto ticketDto = ticketService.getCurrentUserTicket(userId);
        assertEquals(ticket.getUser().getId(), ticketDto.getUser_id());
        assertEquals(ticket.getIssuedAt(), ticketDto.getIssuedAt());

    }

    @Test
    public void getCurrentTicket_WhenUserDoesNotHaveCurrentTicket_ShouldReturnNull() {
        Long userId = 0L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUserAndIssuedAtBefore(Mockito.any(User.class), Mockito.any(LocalDateTime.class))).thenReturn(Collections.emptyList());
        assertNull(ticketService.getCurrentUserTicket(userId));


    }

    @Test
    public void getCurrentTicket_WhenUserDoesNotExist_ShouldThrowEntityNotFoundException(){
        Long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> ticketService.getCurrentUserTicket(userId));

    }

}
