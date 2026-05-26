package com.tanmay.helpdesk_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tanmay.helpdesk_backend.dto.TicketRequestDto;
import com.tanmay.helpdesk_backend.entity.Ticket;
import com.tanmay.helpdesk_backend.enums.TicketCategory;
import com.tanmay.helpdesk_backend.enums.TicketPriority;
import com.tanmay.helpdesk_backend.enums.TicketStatus;
import com.tanmay.helpdesk_backend.service.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tickets")

public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public Ticket createTicket(
        @Valid
        @RequestBody
        TicketRequestDto dto) {

       return ticketService.createTicket(dto);
    }

    @GetMapping
    public List<Ticket> getAllTickets() {

         return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(
        @PathVariable Long id) {

      return ticketService.getTicketById(id);
    }

    @GetMapping("/status/{status}")
    public List<Ticket> getTicketsByStatus(
        @PathVariable TicketStatus status) {

      return ticketService
            .getTicketsByStatus(status);
    }

    @GetMapping("/priority/{priority}")
    public List<Ticket> getTicketsByPriority(
        @PathVariable
        TicketPriority priority) {

       return ticketService
            .getTicketsByPriority(
                    priority);
    }

    @GetMapping("/category/{category}")
    public List<Ticket> getTicketsByCategory(
        @PathVariable
        TicketCategory category) {

        return ticketService
            .getTicketsByCategory(
                    category);
    }

    @GetMapping("/search/{keyword}")
    public List<Ticket> searchTickets(
        @PathVariable String keyword) {

      return ticketService
            .searchTickets(keyword);
    }

    @GetMapping("/paginated")
    public Page<Ticket> getPaginatedTickets(

        @RequestParam int page,

        @RequestParam int size) {

       return ticketService
            .getPaginatedTickets(
                    page,
                    size);
    }

    @GetMapping("/count")
    public Map<String, Long> getTotalTicketCount() {

       Map<String, Long> response =
            new HashMap<>();

       response.put(
            "totalTickets",

            ticketService
                    .getTotalTicketCount());

      return response;
   }

      @GetMapping("/open-count")
      public Map<String, Long>
      getOpenTicketCount() {

         Map<String, Long> response =
            new HashMap<>();

           response.put(
            "openTickets",

            ticketService
                    .getOpenTicketCount());

         return response;
    }

    @GetMapping("/high-priority-count")
    public Map<String, Long>
       getHighPriorityTicketCount() {

         Map<String, Long> response =
            new HashMap<>();

          response.put(
            "highPriorityTickets",

            ticketService
                    .getHighPriorityTicketCount());

     return response;
   }

    @PutMapping("/user/{id}")
    public Ticket updateUserTicket(
        @PathVariable Long id,
        @RequestBody Ticket updatedTicket) {

        return ticketService
            .updateUserTicket(id, updatedTicket);
   }

   @PreAuthorize("hasRole('ADMIN')")
   @PutMapping("/admin/{id}")
    public Ticket updateAdminTicket(
        @PathVariable Long id,
        @RequestBody Ticket updatedTicket) {

    return ticketService
            .updateAdminTicket(id, updatedTicket);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
     public String deleteTicket(
        @PathVariable Long id) {

    return ticketService.deleteTicket(id);
    }
    
}
