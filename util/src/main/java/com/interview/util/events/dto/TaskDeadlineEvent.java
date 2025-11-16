package com.interview.util.events.dto;

import java.time.LocalDate;

public record TaskDeadlineEvent(
                Long taskId,
                String description,
                LocalDate dueDate,
                Long contactId,
                String message,
                EventType eventType) {
}