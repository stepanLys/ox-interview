package com.interview.crm.client.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientRequest(
        @NotBlank String companyName,
        String industry,
        String address) {
}
