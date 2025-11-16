package com.interview.crm.contact.dto;

public record ContactResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Long clientId) {
}
