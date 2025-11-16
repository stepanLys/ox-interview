package com.interview.crm.client.dto;

import java.io.Serializable;
import java.util.List;

import com.interview.crm.contact.dto.ContactInfo;

public record ClientResponse(
        Long id,
        String companyName,
        String industry,
        String address,
        List<ContactInfo> contacts) implements Serializable {
}
