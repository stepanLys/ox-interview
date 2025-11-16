package com.interview.util.events.dto;

public record TaskStatusChangedEvent(
                Long taskId,
                TaskStatus newStatus,
                Long contactId,
                String taskDescription,
                EventType eventType) {
}
