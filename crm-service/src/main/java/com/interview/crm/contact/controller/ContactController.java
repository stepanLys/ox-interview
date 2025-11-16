package com.interview.crm.contact.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.interview.crm.contact.dto.ClientContactResponse;
import com.interview.crm.contact.dto.ContactCriteria;
import com.interview.crm.contact.dto.ContactResponse;
import com.interview.crm.contact.dto.CreateContactRequest;
import com.interview.crm.contact.dto.UpdateContactRequest;
import com.interview.crm.contact.service.ContactService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/contacts")
@RequiredArgsConstructor
@Tag(name = "Contact Management", description = "API for managing contacts")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @Operation(summary = "Create a new contact", description = "Creates a new contact with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contact created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ContactResponse> createContact(@Valid @RequestBody CreateContactRequest request) {
        ContactResponse response = contactService.createContact(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get contact by ID", description = "Retrieves a contact by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contact retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Long id) {
        ContactResponse response = contactService.getContactById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("client/{clientId}")
    @Operation(summary = "Get contacts by client ID", description = "Retrieves all contacts associated with a specific client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contacts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ClientContactResponse> getContactsByClientId(@PathVariable Long clientId,
            @RequestBody ContactCriteria criteria) {
        ClientContactResponse response = contactService.getContactsByClientId(clientId, criteria);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update contact", description = "Updates an existing contact with new information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contact updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    public ResponseEntity<ContactResponse> updateContact(@PathVariable Long id,
            @Valid @RequestBody UpdateContactRequest request) {
        ContactResponse response = contactService.updateContact(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete contact", description = "Deletes a contact by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contact deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
    }
}