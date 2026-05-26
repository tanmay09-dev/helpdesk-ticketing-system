package com.tanmay.helpdesk_backend.dto;

import com.tanmay.helpdesk_backend.enums.TicketCategory;
import com.tanmay.helpdesk_backend.enums.TicketPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TicketRequestDto {

    @NotBlank(message =
            "Title is required")
    private String title;

    @NotBlank(message =
            "Description is required")
    private String description;

    @NotNull(message =
            "Priority is required")
    private TicketPriority priority;

    @NotNull(message = "Category is required")
    private TicketCategory category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description) {

        this.description = description;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(
            TicketPriority priority) {

        this.priority = priority;
    }

    public TicketCategory getCategory() {
    return category;
    }

    public void setCategory(
        TicketCategory category) {

        this.category = category;
    }
}