package com.interview.crm.client.controller;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.interview.crm.client.dto.AllClientResponse;
import com.interview.crm.client.dto.ClientCriteria;
import com.interview.crm.client.dto.ClientRequest;
import com.interview.crm.client.dto.ClientResponse;
import com.interview.crm.client.service.ClientService;
import com.interview.crm.common.export.ExportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("api/clients")
@RestController
@Tag(name = "Client Management", description = "API for managing clients")
public class ClientController {

    private final ClientService clientService;
    private final ExportService exportService;

    @PostMapping
    @Operation(summary = "Create a new client", description = "Creates a new client with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid client data provided")
    })
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest clientRequest) {
        var createdClient = clientService.saveClient(clientRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing client", description = "Updates an existing client with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid client data provided"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequest clientRequest) {
        var updatedClient = clientService.updateClient(id, clientRequest);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a client", description = "Deletes a client by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Retrieves a client by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        var client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping("search")
    @Operation(summary = "Get all clients", description = "Retrieves a list of all clients")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully")
    })
    public ResponseEntity<AllClientResponse> getAllClients(@RequestBody ClientCriteria criteria) {
        var clients = clientService.getAllClients(criteria);
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/export")
    @Operation(summary = "Export clients data", description = "Exports client data based on search criteria as Excel or PDF")
    public ResponseEntity<InputStreamResource> exportClients(
            @RequestBody ClientCriteria criteria,
            @RequestParam String format) throws IOException {

        var clientListResponse = clientService.getAllClients(criteria);
        var clients = clientListResponse.clients();
        var strategy = exportService.getClientStrategy(format);
        var export = strategy.export(clients);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=clients" + strategy.getFileExtension());

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(strategy.getMimeType()))
                .body(new InputStreamResource(export));
    }

}