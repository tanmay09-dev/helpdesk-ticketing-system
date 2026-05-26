package com.tanmay.helpdesk_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanmay.helpdesk_backend.dto.CommentRequestDto;
import com.tanmay.helpdesk_backend.entity.Comment;
import com.tanmay.helpdesk_backend.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{ticketId}")
    public Comment addComment(
            @PathVariable Long ticketId,

            @Valid
            @RequestBody
            CommentRequestDto dto) {

        return commentService.addComment(
                ticketId,
                dto);
    }
    @GetMapping("/{ticketId}")
    public List<Comment> getComments(
        @PathVariable Long ticketId) {

        return commentService
            .getCommentsByTicket(ticketId);
        }
}