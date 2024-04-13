package com.example.backend.Service;

import com.example.backend.Exception.EntityNotFoundException;
import com.example.backend.Exception.HangTicketException;
import com.example.backend.Exception.TicketPurchaseNotValidException;
import com.example.backend.Repository.TicketRepository;
import com.example.backend.Repository.TicketSuspensionRepository;
import com.example.backend.Repository.UserRepository;
import com.example.backend.Utils.TicketType;
import com.example.backend.dto.TicketDto;
import com.example.backend.entity.GymTicket;
import com.example.backend.entity.TicketSuspension;
import com.example.backend.entity.User;
import com.example.backend.mappers.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final UserRepository userRepository;

    private final TicketRepository ticketRepository;

    private final TicketSuspensionRepository ticketSuspensionRepository;

    public TicketDto addTicket(Long userId, TicketType ticketType) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
        LocalDateTime expirationAt;
        LocalDateTime issuedAt = LocalDateTime.now();
        List<GymTicket> gymTicketList = ticketRepository.findAllByUser(user);
        List<GymTicket> criticalList = gymTicketList.stream().filter(gymTicket -> gymTicket.getExpirationAt().isAfter(issuedAt) && gymTicket.getIssuedAt().isBefore(issuedAt)).toList();
        List<GymTicket> potentialTicketList = gymTicketList.stream().filter(gymTicket -> gymTicket.getExpirationAt().isAfter(issuedAt)).toList();
        if (!criticalList.isEmpty()) {
            throw new TicketPurchaseNotValidException("You currently have a ticket purchased");
        }
        if (ticketType == TicketType.ONE_MONTH) {
            if (isAnyOtherTicketWithinPeriode(issuedAt, potentialTicketList, 1, ChronoUnit.MONTHS)) {
                throw new TicketPurchaseNotValidException("you already have a ticket that starts within 1 month");
            }
            expirationAt = issuedAt.plusMonths(1);
        } else if (ticketType == TicketType.THREE_MONTH) {
            if (isAnyOtherTicketWithinPeriode(issuedAt, potentialTicketList, 3, ChronoUnit.MONTHS)) {
                throw new TicketPurchaseNotValidException("you already have a ticket that starts within 3 months");
            }
            expirationAt = issuedAt.plusMonths(3);
        } else {
            if (isAnyOtherTicketWithinPeriode(issuedAt, potentialTicketList, 1, ChronoUnit.YEARS)) {
                throw new TicketPurchaseNotValidException("you already have a ticket that starts within 1 year");
            }
            expirationAt = issuedAt.plusYears(1);
        }

        GymTicket gymTicket = GymTicket.builder()
                .ticketType(ticketType)
                .issuedAt(issuedAt)
                .expirationAt(expirationAt)
                .user(user)
                .build();

        GymTicket savedGymTicket = ticketRepository.save(gymTicket);
        return TicketMapper.mapGymTicketToTicketDTo(savedGymTicket);

    }

    public TicketDto addTicket(Long userId, TicketType ticketType, LocalDateTime plannedIssuedAt) {


        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
        List<GymTicket> gymTicketList = ticketRepository.findAllByUser(user);
        List<GymTicket> criticalList = gymTicketList.stream().filter(gymTicket -> gymTicket.getExpirationAt().isAfter(plannedIssuedAt) && gymTicket.getIssuedAt().isBefore(plannedIssuedAt)).collect(Collectors.toList());
        List<GymTicket> potentialTicketList = gymTicketList.stream().filter(gymTicket -> gymTicket.getExpirationAt().isAfter(plannedIssuedAt)).collect(Collectors.toList());
        LocalDateTime expirationAt;

        if (!criticalList.isEmpty()) {
            throw new TicketPurchaseNotValidException("You currently have a ticket purchased");
        }

        if (ticketType == TicketType.ONE_MONTH) {
            if (isAnyOtherTicketWithinPeriode(plannedIssuedAt, potentialTicketList, 1, ChronoUnit.MONTHS)) {
                throw new TicketPurchaseNotValidException("you already have a ticket that starts within 1 month of your intended purchase date");
            }
            expirationAt = plannedIssuedAt.plusMonths(1);
        } else if (ticketType == TicketType.THREE_MONTH) {
            if (isAnyOtherTicketWithinPeriode(plannedIssuedAt, potentialTicketList, 3, ChronoUnit.MONTHS)) {
                throw new TicketPurchaseNotValidException("you already have a ticket that starts within 3 months of your intended purchase date");

            }
            expirationAt = plannedIssuedAt.plusMonths(3);
        } else {
            if (isAnyOtherTicketWithinPeriode(plannedIssuedAt, potentialTicketList, 1, ChronoUnit.YEARS)) {
                throw new TicketPurchaseNotValidException("you already have a ticket that starts within 1 year of your intended purchase date");

            }
            expirationAt = plannedIssuedAt.plusYears(1);
        }

        GymTicket gymTicket = GymTicket.builder()
                .ticketType(ticketType)
                .issuedAt(plannedIssuedAt)
                .expirationAt(expirationAt)
                .user(user)
                .build();

        GymTicket savedGymTicket = ticketRepository.save(gymTicket);
        return TicketMapper.mapGymTicketToTicketDTo(savedGymTicket);


    }

    private static boolean isAnyOtherTicketWithinPeriode(LocalDateTime offset, List<GymTicket> potentialTickets, int amount, TemporalUnit temporalUnit) {
        return potentialTickets.stream().anyMatch(gymTicket -> gymTicket.getIssuedAt().isBefore(offset.plus(amount, temporalUnit)));
    }

    @Override
    public List<TicketDto> getAllUserTickets(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));

        List<GymTicket> gymTicketList = ticketRepository.findAllByUser(user);
        if (!gymTicketList.isEmpty()) {
            return gymTicketList.stream().map(TicketMapper::mapGymTicketToTicketDTo).collect(Collectors.toList());

        }
        return Collections.emptyList();

    }


    @Override
    public TicketDto getSingleTicket(Long ticketId) {
        GymTicket gymTicket = ticketRepository.findById(ticketId).orElseThrow(() -> new EntityNotFoundException(GymTicket.class, ticketId));
        return TicketMapper.mapGymTicketToTicketDTo(gymTicket);
    }

    @Override
    public void hangYourTicket(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));

        LocalDateTime now = LocalDateTime.now();
        GymTicket presentGymTicket = ticketRepository.findAllByUserAndIssuedAtBefore(user, now).stream().filter(gymTicket -> gymTicket.getExpirationAt().isAfter(now)).findFirst().orElseThrow(() -> new HangTicketException("You don't have currently ticket"));
        List<GymTicket> futureTickets = ticketRepository.findAllByUserAndIssuedAtAfter(user, presentGymTicket.getExpirationAt());
        if (futureTickets.isEmpty() || futureTickets.stream().noneMatch(gymTicket -> gymTicket.getIssuedAt().isBefore(presentGymTicket.getExpirationAt().plusWeeks(1)))) {
            presentGymTicket.setExpirationAt(presentGymTicket.getExpirationAt().plusWeeks(1));
            TicketSuspension ticketSuspension = TicketSuspension.builder()
                    .issuedAt(now)
                    .expiredAt(now.plusMinutes(1))
                    .gymTicket(presentGymTicket)
                    .build();
            ticketSuspensionRepository.save(ticketSuspension);
            presentGymTicket.setSuspension(ticketSuspension);
            ticketRepository.save(presentGymTicket);

        }else {
            throw new HangTicketException("You have another ticket in this time period");
        }

    }

    @Override
    public TicketDto getCurrentUserTicket(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));

        Optional<GymTicket> gymTicket = ticketRepository.findAllByUserAndIssuedAtBefore(user, LocalDateTime.now()).stream().filter(gymTicket1 -> gymTicket1.getExpirationAt().isAfter(LocalDateTime.now())).findFirst();
        return gymTicket.map(TicketMapper::mapGymTicketToTicketDTo).orElse(null);

    }

}