package com.tanmay.helpdesk_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanmay.helpdesk_backend.entity.Comment;
import com.tanmay.helpdesk_backend.entity.Ticket;

@Repository
public interface CommentRepository
        extends JpaRepository<Comment, Long> {

    List<Comment> findByTicket(Ticket ticket);
}