package com.interview.crm.contact.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateContactRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email String email,
        @Pattern(regexp = "(^$|[0-9]{10})") String phone,
        @NotNull Long clientId) {
}
