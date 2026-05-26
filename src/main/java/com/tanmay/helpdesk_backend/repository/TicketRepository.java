package com.tanmay.helpdesk_backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanmay.helpdesk_backend.entity.Ticket;
import com.tanmay.helpdesk_backend.entity.User;
import com.tanmay.helpdesk_backend.enums.TicketCategory;
import com.tanmay.helpdesk_backend.enums.TicketPriority;
import com.tanmay.helpdesk_backend.enums.TicketStatus;

@Repository
public interface TicketRepository
        extends JpaRepository<Ticket, Long> {

                List<Ticket> findByUser(User user);

                List<Ticket> findByStatus(
                TicketStatus status);

                List<Ticket> findByPriority(
                TicketPriority priority);

                List<Ticket> findByCategory(
                TicketCategory category);


                List<Ticket> findByTitleContainingIgnoreCase(
                String keyword);

                Page<Ticket> findByUser(
                User user,
               Pageable pageable);

               Page<Ticket> findAll(
               Pageable pageable);

               long countByStatus(
                 TicketStatus status);

                 long countByPriority(
                  TicketPriority priority);

}
