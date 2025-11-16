package com.interview.crm.client.mapper;

import com.interview.crm.client.dto.ClientRequest;
import com.interview.crm.client.dto.ClientResponse;
import com.interview.crm.client.model.Client;
import com.interview.crm.common.EntityMapper;
import com.interview.crm.contact.mapper.ContactMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ContactMapper.class)
public interface ClientMapper extends EntityMapper<Client, ClientResponse> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    Client toEntity(ClientRequest clientRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    Client toEntity(@MappingTarget Client client, ClientRequest clientRequest);

    default Client fromId(Long id) {
        if (id == null) {
            return null;
        }

        var entity = new Client();
        entity.setId(id);

        return entity;
    }
}
