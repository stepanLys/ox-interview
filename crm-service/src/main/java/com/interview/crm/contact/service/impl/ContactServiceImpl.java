package com.interview.crm.contact.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.interview.crm.client.mapper.ClientMapper;
import com.interview.crm.common.exception.ResourceNotFoundException;
import com.interview.crm.contact.dto.ClientContactResponse;
import com.interview.crm.contact.dto.ContactCriteria;
import com.interview.crm.contact.dto.ContactResponse;
import com.interview.crm.contact.dto.CreateContactRequest;
import com.interview.crm.contact.dto.UpdateContactRequest;
import com.interview.crm.contact.mapper.ContactMapper;
import com.interview.crm.contact.model.Contact;
import com.interview.crm.contact.repository.ContactRepository;
import com.interview.crm.contact.service.ContactService;
import com.interview.crm.contact.service.specification.ContactSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final ClientMapper clientMapper;

    @Override
    public ContactResponse createContact(CreateContactRequest request) {
        log.info("Creating contact for client id: {}", request.clientId());

        var contact = contactMapper.toEntity(request);
        contact.setClient(clientMapper.fromId(request.clientId()));

        return contactMapper.toDto(contactRepository.save(contact));
    }

    @Override
    public ContactResponse getContactById(Long id) {
        var contact = findContactById(id);
        return contactMapper.toDto(contact);
    }

    @Cacheable(value = "contacts", key = "#p0 + '-' + #p1")
    @Override
    public ClientContactResponse getContactsByClientId(Long clientId, ContactCriteria criteria) {
        log.debug("Fetching all contacts for client id: {}", clientId);
        var spec = ContactSpecification.byCriteria(clientId, criteria);
        return new ClientContactResponse(contactMapper.toDtoList(contactRepository.findAll(spec)));
    }

    @CacheEvict(value = { "clients" }, allEntries = true)
    @Override
    public ContactResponse updateContact(Long id, UpdateContactRequest request) {
        log.info("Updating contact id: {}", id);

        var existingContact = findContactById(id);
        contactMapper.toEntity(existingContact, request);

        return contactMapper.toDto(contactRepository.save(existingContact));
    }

    @CacheEvict(value = { "clients" }, allEntries = true)
    @Override
    public void deleteContact(Long id) {
        log.info("Deleting contact id: {}", id);
        var contact = findContactById(id);
        contactRepository.delete(contact);
    }

    private Contact findContactById(Long contactId) {
        return contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + contactId));
    }

}
