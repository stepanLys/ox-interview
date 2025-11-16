package com.interview.crm.task.dto;

import java.time.LocalDate;

import com.interview.crm.contact.dto.ContactResponse;
import com.interview.util.events.dto.TaskStatus;

public record TaskResponse(
        Long id,
        String description,
        TaskStatus status,
        LocalDate dueDate,
        ContactResponse contact) {
}
