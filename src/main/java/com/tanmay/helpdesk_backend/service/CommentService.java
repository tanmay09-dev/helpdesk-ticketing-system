package com.tanmay.helpdesk_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tanmay.helpdesk_backend.dto.CommentRequestDto;
import com.tanmay.helpdesk_backend.entity.Comment;
import com.tanmay.helpdesk_backend.entity.Ticket;
import com.tanmay.helpdesk_backend.entity.User;
import com.tanmay.helpdesk_backend.exception.ResourceNotFoundException;
import com.tanmay.helpdesk_backend.repository.CommentRepository;
import com.tanmay.helpdesk_backend.repository.TicketRepository;
import com.tanmay.helpdesk_backend.repository.UserRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment addComment(
            Long ticketId,
            CommentRequestDto dto) {

        Ticket ticket =
                ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket Not Found"));

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user =
                userRepository.findByEmail(email);

        Comment comment = new Comment();

        comment.setMessage(dto.getMessage());

        comment.setCreatedAt(
                LocalDateTime.now());

        comment.setTicket(ticket);

        comment.setUser(user);

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByTicket(
        Long ticketId) {

          Ticket ticket =
                ticketRepository.findById(ticketId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException(
                            "Ticket Not Found"));

        return commentRepository.findByTicket(ticket);
        }
}