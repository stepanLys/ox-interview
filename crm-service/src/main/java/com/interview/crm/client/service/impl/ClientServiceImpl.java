package com.interview.crm.client.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.interview.crm.client.dto.AllClientResponse;
import com.interview.crm.client.dto.ClientCriteria;
import com.interview.crm.client.dto.ClientRequest;
import com.interview.crm.client.dto.ClientResponse;
import com.interview.crm.client.mapper.ClientMapper;
import com.interview.crm.client.model.Client;
import com.interview.crm.client.repository.ClientRepository;
import com.interview.crm.client.service.ClientService;
import com.interview.crm.client.service.specification.ClientSpecification;
import com.interview.crm.common.exception.BusinessLogicException;
import com.interview.crm.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    @CacheEvict(value = "clients", allEntries = true)
    public ClientResponse saveClient(ClientRequest clientRequest) {
        log.debug("Saving client with request: {}", clientRequest);

        if (clientRequest == null) {
            throw new BusinessLogicException("Client request cannot be null");
        }

        var client = clientMapper.toEntity(clientRequest);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @CacheEvict(value = "clients", allEntries = true)
    @Override
    public ClientResponse updateClient(Long id, ClientRequest clientRequest) {
        log.debug("Updating client with id: {} and request: {}", id, clientRequest);

        var client = findClientById(id);
        client = clientMapper.toEntity(client, clientRequest);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    @CacheEvict(value = "clients", allEntries = true)
    public void deleteClient(Long clientId) {
        log.debug("Deleting client with id: {}", clientId);
        var client = findClientById(clientId);
        clientRepository.delete(client);
    }

    @Cacheable(value = "clients", key = "#p0")
    @Override
    public ClientResponse getClientById(Long clientId) {
        log.debug("Retrieving client with id: {}", clientId);

        var client = findClientById(clientId);
        return clientMapper.toDto(client);
    }

    @Cacheable(value = "clients", key = "#p0")
    @Override
    public AllClientResponse getAllClients(ClientCriteria criteria) {
        var spec = ClientSpecification.byCriteria(criteria);
        return new AllClientResponse(clientMapper.toDtoList(clientRepository.findAll(spec)));
    }

    private Client findClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    log.warn("Client not found with id: {}", clientId);
                    return new ResourceNotFoundException("Client not found with id: " + clientId);
                });
    }
}