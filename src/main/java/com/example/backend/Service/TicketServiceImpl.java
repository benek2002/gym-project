package com.example.backend.Service;

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

        Optional<User> user = userRepository.findById(userId);
        LocalDateTime expirationAt;
        LocalDateTime issuedAt = LocalDateTime.now();
        List<GymTicket> gymTicketList = ticketRepository.findAllByUser(user.get());
        List<GymTicket> criticalList = gymTicketList.stream().filter(gymTicket -> gymTicket.getExpirationAt().isAfter(issuedAt) && gymTicket.getIssuedAt().isBefore(issuedAt)).collect(Collectors.toList());
        List<GymTicket> potentialTicketList = gymTicketList.stream().filter(gymTicket -> gymTicket.getExpirationAt().isAfter(issuedAt)).collect(Collectors.toList());
        if (user.isPresent()) {
            if (criticalList.size() == 0) {
                if (ticketType == TicketType.ONE_MONTH) {
                    if (potentialTicketList.stream().filter(gymTicket -> gymTicket.getIssuedAt().isBefore(issuedAt.plusMonths(1))).collect(Collectors.toList()).size() != 0) {
                        return null;
                    }
                    expirationAt = issuedAt.plusMonths(1);
                } else if (ticketType == TicketType.THREE_MONTH) {
                    if (potentialTicketList.stream().filter(gymTicket -> gymTicket.getIssuedAt().isBefore(issuedAt.plusMonths(3))).collect(Collectors.toList()).size() != 0) {
                        return null;
                    }
                    expirationAt = issuedAt.plusMonths(3);
                } else {
                    if (potentialTicketList.stream().filter(gymTicket -> gymTicket.getIssuedAt().isBefore(issuedAt.plusYears(1))).collect(Collectors.toList()).size() != 0) {
                        return null;
                    }
                    expirationAt = issuedAt.plusYears(1);
                }

                GymTicket gymTicket = GymTicket.builder()
                        .ticketType(ticketType)
                        .issuedAt(issuedAt)
                        .expirationAt(expirationAt)
                        .user(user.get())
                        .build();

                GymTicket savedGymTicket = ticketRepository.save(gymTicket);
                return TicketMapper.mapGymTicketToTicketDTo(savedGymTicket);

            }
        }
        return null;


    }

    public TicketDto addTicket(Long userId, TicketType ticketType, LocalDateTime dateTime) {

        LocalDateTime issuedAt = LocalDateTime.now();
        Optional<User> user = userRepository.findById(userId);
        List<GymTicket> gymTicketList = ticketRepository.findAllByUser(user.get());
        List<GymTicket> criticalList = gymTicketList.stream().filter(gymTicket -> gymTicket.getExpirationAt().isAfter(dateTime) && gymTicket.getIssuedAt().isBefore(dateTime)).collect(Collectors.toList());
        List<GymTicket> potentialTicketList = gymTicketList.stream().filter(gymTicket -> gymTicket.getExpirationAt().isAfter(dateTime)).collect(Collectors.toList());
        LocalDateTime expirationAt;
        if (user.isPresent()) {
            if (criticalList.size() == 0) {
                if (ticketType == TicketType.ONE_MONTH) {
                    if (potentialTicketList.stream().filter(gymTicket -> gymTicket.getIssuedAt().isBefore(dateTime.plusMonths(1))).collect(Collectors.toList()).size() != 0) {
                        return null;
                    }
                    expirationAt = dateTime.plusMonths(1);
                } else if (ticketType == TicketType.THREE_MONTH) {
                    if (potentialTicketList.stream().filter(gymTicket -> gymTicket.getIssuedAt().isBefore(dateTime.plusMonths(3))).collect(Collectors.toList()).size() != 0) {
                        return null;
                    }
                    expirationAt = dateTime.plusMonths(3);
                } else {
                    if (potentialTicketList.stream().filter(gymTicket -> gymTicket.getIssuedAt().isBefore(dateTime.plusYears(1))).collect(Collectors.toList()).size() != 0) {
                        return null;
                    }
                    expirationAt = dateTime.plusYears(1);
                }

                GymTicket gymTicket = GymTicket.builder()
                        .ticketType(ticketType)
                        .issuedAt(dateTime)
                        .expirationAt(expirationAt)
                        .user(user.get())
                        .build();

                GymTicket savedGymTicket = ticketRepository.save(gymTicket);
                return TicketMapper.mapGymTicketToTicketDTo(savedGymTicket);
            }

        }
        return null;


    }

    @Override
    public List<TicketDto> getAllUserTickets(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            List<GymTicket> gymTicketList = ticketRepository.findAllByUser(optionalUser.get());
            if (!gymTicketList.isEmpty()) {
                return gymTicketList.stream().map(TicketMapper::mapGymTicketToTicketDTo).collect(Collectors.toList());

            }
        }
        return null;
    }



    @Override
    public TicketDto getSingleTicket(Long ticketId) {
        Optional<GymTicket> optionalGymTicket = ticketRepository.findById(ticketId);
        return optionalGymTicket.map(TicketMapper::mapGymTicketToTicketDTo).orElse(null);
    }

    @Override
    public String hangYourTicket(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return "User with this id not found";
        }
        LocalDateTime now = LocalDateTime.now();
        Optional<GymTicket> optionalPresentGymTicket = ticketRepository.findAllByUserAndIssuedAtBefore(optionalUser.get(), now).stream().filter(gymTicket -> gymTicket.getExpirationAt().isAfter(now)).findFirst();
        if (optionalPresentGymTicket.isPresent()) {
            List<GymTicket> futureTickets = ticketRepository.findAllByUserAndIssuedAtAfter(optionalUser.get(), optionalPresentGymTicket.get().getExpirationAt());
            if (futureTickets.size() == 0) {
                optionalPresentGymTicket.get().setExpirationAt(optionalPresentGymTicket.get().getExpirationAt().plusWeeks(1));
                TicketSuspension ticketSuspension = TicketSuspension.builder()
                        .issuedAt(now)
                        .expiredAt(now.plusMinutes(1))
                        .gymTicket(optionalPresentGymTicket.get())
                        .build();
                ticketSuspensionRepository.save(ticketSuspension);
                optionalPresentGymTicket.get().setSuspension(ticketSuspension);
                ticketRepository.save(optionalPresentGymTicket.get());
                return "Your ticket has been successfully suspended for a week";
            }
            if (futureTickets.stream().filter(gymTicket -> gymTicket.getIssuedAt().isBefore(optionalPresentGymTicket.get().getExpirationAt().plusWeeks(1))).collect(Collectors.toList()).size() == 0) {

                optionalPresentGymTicket.get().setExpirationAt(optionalPresentGymTicket.get().getExpirationAt().plusWeeks(1));
                TicketSuspension ticketSuspension = TicketSuspension.builder()
                        .issuedAt(now)
                        .expiredAt(now.plusWeeks(1))
                        .gymTicket(optionalPresentGymTicket.get())
                        .build();
                ticketSuspensionRepository.save(ticketSuspension);
                optionalPresentGymTicket.get().setSuspension(ticketSuspension);
                ticketRepository.save(optionalPresentGymTicket.get());
                return "Your ticket has been successfully suspended for a week";

            }
            return "Bad request. You have another ticket in this time period";

        }
        return "You don't currently have a ticket";
    }

    @Override
    public TicketDto getCurrentUserTicket(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Optional<GymTicket> gymTicket = ticketRepository.findAllByUserAndIssuedAtBefore(optionalUser.get(), LocalDateTime.now()).stream().filter(gymTicket1 -> gymTicket1.getExpirationAt().isAfter(LocalDateTime.now())).findFirst();
            if (gymTicket.isPresent()) {
                return TicketMapper.mapGymTicketToTicketDTo(gymTicket.get());
            }
        }
        return null;
    }
}