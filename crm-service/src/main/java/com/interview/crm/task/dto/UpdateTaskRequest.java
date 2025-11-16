package com.interview.crm.task.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

public record UpdateTaskRequest(
        @NotBlank String description,
        @FutureOrPresent LocalDate dueDate) {
}