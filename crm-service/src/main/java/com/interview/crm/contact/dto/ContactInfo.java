package com.interview.crm.contact.dto;

public record ContactInfo(
        String firstName,
        String lastName,
        String email,
        String phone) {
}