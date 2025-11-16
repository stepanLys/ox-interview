package com.interview.crm.task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.interview.crm.common.EntityMapper;
import com.interview.crm.contact.mapper.ContactMapper;
import com.interview.crm.task.dto.CreateTaskRequest;
import com.interview.crm.task.dto.TaskResponse;
import com.interview.crm.task.dto.UpdateTaskRequest;
import com.interview.crm.task.model.Task;

@Mapper(componentModel = "spring", uses = { ContactMapper.class })
public interface TaskMapper extends EntityMapper<Task, TaskResponse> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "contact", ignore = true)
    Task toEntity(CreateTaskRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "contact", ignore = true)
    void toEntity(@MappingTarget Task task, UpdateTaskRequest request);
}
