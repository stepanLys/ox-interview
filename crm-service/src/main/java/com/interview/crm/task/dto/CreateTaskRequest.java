package com.interview.crm.task.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskRequest(
        @NotBlank String description,
        @FutureOrPresent LocalDate dueDate,
        @NotNull Long contactId) {
}