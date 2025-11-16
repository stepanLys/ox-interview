package com.interview.crm.contact.service;

import com.interview.crm.contact.dto.ClientContactResponse;
import com.interview.crm.contact.dto.ContactCriteria;
import com.interview.crm.contact.dto.ContactResponse;
import com.interview.crm.contact.dto.CreateContactRequest;
import com.interview.crm.contact.dto.UpdateContactRequest;

public interface ContactService {
    ContactResponse createContact(CreateContactRequest request);

    ContactResponse getContactById(Long id);

    ClientContactResponse getContactsByClientId(Long clientId, ContactCriteria criteria);

    ContactResponse updateContact(Long id, UpdateContactRequest request);

    void deleteContact(Long id);
}
