package com.interview.crm.common;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface EntityMapper<ENTITY, DTO> {

    default List<DTO> toDtoList(Collection<ENTITY> entities) {
        return entities == null
                ? Collections.emptyList()
                : entities
                        .stream()
                        .map(this::toDto)
                        .toList();
    }

    DTO toDto(ENTITY entity);
}
