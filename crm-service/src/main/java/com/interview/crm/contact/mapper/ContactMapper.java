package com.interview.crm.contact.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.interview.crm.common.EntityMapper;
import com.interview.crm.contact.dto.ContactInfo;
import com.interview.crm.contact.dto.ContactResponse;
import com.interview.crm.contact.dto.CreateContactRequest;
import com.interview.crm.contact.dto.UpdateContactRequest;
import com.interview.crm.contact.model.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper extends EntityMapper<Contact, ContactResponse> {

    @Mapping(source = "client.id", target = "clientId")
    ContactResponse toDto(Contact contact);

    ContactInfo toContactInfo(Contact contact);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Contact toEntity(CreateContactRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    void toEntity(@MappingTarget Contact contact, UpdateContactRequest request);

    default Contact fromId(Long id) {
        if (id == null) {
            return null;
        }

        var entity = new Contact();
        entity.setId(id);

        return entity;
    }
}
