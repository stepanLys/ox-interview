package com.interview.crm.client.service;

import com.interview.crm.client.dto.AllClientResponse;
import com.interview.crm.client.dto.ClientCriteria;
import com.interview.crm.client.dto.ClientRequest;
import com.interview.crm.client.dto.ClientResponse;

public interface ClientService {

    ClientResponse saveClient(ClientRequest clientRequest);
    
    ClientResponse updateClient(Long id, ClientRequest clientRequest);

    void deleteClient(Long clientId);

    ClientResponse getClientById(Long clientId);

    AllClientResponse getAllClients(ClientCriteria criteria);

}
