package com.interview.crm.task.dto;

import com.interview.util.events.dto.TaskStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(@NotNull TaskStatus status) {
}
