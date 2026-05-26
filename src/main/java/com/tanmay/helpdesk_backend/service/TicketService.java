package com.tanmay.helpdesk_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tanmay.helpdesk_backend.dto.TicketRequestDto;
import com.tanmay.helpdesk_backend.entity.Ticket;
import com.tanmay.helpdesk_backend.entity.User;
import com.tanmay.helpdesk_backend.enums.TicketCategory;
import com.tanmay.helpdesk_backend.enums.TicketPriority;
import com.tanmay.helpdesk_backend.enums.TicketStatus;
import com.tanmay.helpdesk_backend.exception.ResourceNotFoundException;
import com.tanmay.helpdesk_backend.exception.UnauthorizedException;
import com.tanmay.helpdesk_backend.repository.TicketRepository;
import com.tanmay.helpdesk_backend.repository.UserRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    public Ticket createTicket(TicketRequestDto dto) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    String email = authentication.getName();

    User user =
            userRepository.findByEmail(email);

    Ticket ticket = new Ticket();

    ticket.setTitle(dto.getTitle());

    ticket.setDescription(dto.getDescription());

    ticket.setPriority(dto.getPriority());
    
    ticket.setCategory(dto.getCategory());

    ticket.setStatus(TicketStatus.OPEN);

    ticket.setCreatedAt(LocalDateTime.now());

    ticket.setUpdatedAt(LocalDateTime.now());

    ticket.setUser(user);

    return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    // ADMIN check
    if(authentication.getAuthorities().contains(
            new SimpleGrantedAuthority("ROLE_ADMIN"))) {

        return ticketRepository.findAll();
    }

    // USER
    String email = authentication.getName();

    User user =
            userRepository.findByEmail(email);

    return ticketRepository.findByUser(user);
   }

    public Ticket getTicketById(Long id) {

        return ticketRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Ticket Not Found"));
            }
    public Ticket updateUserTicket(
        Long id,
        Ticket updatedTicket) {

    Ticket existingTicket =
            ticketRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Ticket Not Found"));

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    String email = authentication.getName();

    // ownership check
    if(!existingTicket.getUser()
            .getEmail()
            .equals(email)) {

        throw new UnauthorizedException(
        "You can update only your tickets");
    }

    // USER can update only these
    existingTicket.setTitle(
            updatedTicket.getTitle());

    existingTicket.setDescription(
            updatedTicket.getDescription());

    existingTicket.setUpdatedAt(
            LocalDateTime.now());

    return ticketRepository.save(
            existingTicket);
    }

    public Ticket updateAdminTicket(
        Long id,
        Ticket updatedTicket) {

    Ticket existingTicket =
            ticketRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Ticket Not Found"));

    existingTicket.setStatus(
            updatedTicket.getStatus());

    existingTicket.setPriority(
            updatedTicket.getPriority());

    existingTicket.setCategory(
            updatedTicket.getCategory());

    existingTicket.setUpdatedAt(
            LocalDateTime.now());

    return ticketRepository.save(
            existingTicket);
   }
   
    public String deleteTicket(Long id) {

    ticketRepository.deleteById(id);

    return "Ticket Deleted Successfully";
    }

public List<Ticket> getTicketsByStatus(
        TicketStatus status) {

       Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

        // ADMIN
        if(authentication.getAuthorities().contains(
            new SimpleGrantedAuthority(
                    "ROLE_ADMIN"))) {

        return ticketRepository
                .findByStatus(status);
        }

         // USER
         String email = authentication.getName();

         User user =
            userRepository.findByEmail(email);

         return ticketRepository.findByUser(user)
            .stream()
            .filter(ticket ->
                    ticket.getStatus() == status)
            .toList();
    }

    public List<Ticket> getTicketsByPriority(
        TicketPriority priority) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    // ADMIN
    if(authentication.getAuthorities().contains(
            new SimpleGrantedAuthority(
                    "ROLE_ADMIN"))) {

        return ticketRepository
                .findByPriority(priority);
    }

    // USER
    String email = authentication.getName();

    User user =
            userRepository.findByEmail(email);

    return ticketRepository.findByUser(user)
            .stream()
            .filter(ticket ->
                    ticket.getPriority()
                            == priority)
            .toList();
  }


  public List<Ticket> getTicketsByCategory(
        TicketCategory category) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    // ADMIN
    if(authentication.getAuthorities().contains(
            new SimpleGrantedAuthority(
                    "ROLE_ADMIN"))) {

        return ticketRepository
                .findByCategory(category);
    }

    // USER
    String email = authentication.getName();

    User user =
            userRepository.findByEmail(email);

    return ticketRepository.findByUser(user)
            .stream()
            .filter(ticket ->
                    ticket.getCategory()
                            == category)
            .toList();
   }


   public List<Ticket> searchTickets(
        String keyword) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    // ADMIN
    if(authentication.getAuthorities().contains(
            new SimpleGrantedAuthority(
                    "ROLE_ADMIN"))) {

        return ticketRepository
                .findByTitleContainingIgnoreCase(
                        keyword);
    }

    // USER
    String email = authentication.getName();

    User user =
            userRepository.findByEmail(email);

    return ticketRepository.findByUser(user)
            .stream()
            .filter(ticket ->
                    ticket.getTitle()
                            .toLowerCase()
                            .contains(
                                    keyword.toLowerCase()))
            .toList();
   }

   public Page<Ticket> getPaginatedTickets(
        int page,
        int size) {

    Pageable pageable =
            PageRequest.of(
                    page,
                    size,
                    Sort.by("createdAt")
                            .descending());

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    // ADMIN
    if(authentication.getAuthorities().contains(
            new SimpleGrantedAuthority(
                    "ROLE_ADMIN"))) {

        return ticketRepository.findAll(
                pageable);
    }

    // USER
    String email = authentication.getName();

    User user =
            userRepository.findByEmail(email);

    return ticketRepository.findByUser(
            user,
            pageable);
   }


   public long getTotalTicketCount() {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    // ADMIN
    if(authentication.getAuthorities().contains(
            new SimpleGrantedAuthority(
                    "ROLE_ADMIN"))) {

        return ticketRepository.count();
    }

    // USER
    String email = authentication.getName();

    User user =
            userRepository.findByEmail(email);

    return ticketRepository
            .findByUser(user)
            .size();
  }


  public long getOpenTicketCount() {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    // ADMIN
    if(authentication.getAuthorities().contains(
            new SimpleGrantedAuthority(
                    "ROLE_ADMIN"))) {

        return ticketRepository.countByStatus(
                TicketStatus.OPEN);
    }

    // USER
    String email = authentication.getName();

    User user =
            userRepository.findByEmail(email);

    return ticketRepository.findByUser(user)
            .stream()
            .filter(ticket ->
                    ticket.getStatus()
                            == TicketStatus.OPEN)
            .count();
   }

   public long getHighPriorityTicketCount() {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    // ADMIN
    if(authentication.getAuthorities().contains(
            new SimpleGrantedAuthority(
                    "ROLE_ADMIN"))) {

        return ticketRepository.countByPriority(
                TicketPriority.HIGH);
    }

    // USER
    String email = authentication.getName();

    User user =
            userRepository.findByEmail(email);

    return ticketRepository.findByUser(user)
            .stream()
            .filter(ticket ->
                    ticket.getPriority()
                            == TicketPriority.HIGH)
            .count();
   }
}
