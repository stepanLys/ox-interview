package com.interview.crm.task.service.impl;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interview.crm.common.exception.ResourceNotFoundException;
import com.interview.crm.contact.repository.ContactRepository;
import com.interview.util.events.dto.TaskStatusChangedEvent;
import com.interview.crm.task.dto.CreateTaskRequest;
import com.interview.crm.task.dto.TaskResponse;
import com.interview.crm.task.dto.UpdateTaskRequest;
import com.interview.crm.task.dto.UpdateTaskStatusRequest;
import com.interview.crm.task.mapper.TaskMapper;
import com.interview.crm.task.model.Task;
import com.interview.util.events.dto.EventType;
import com.interview.util.events.dto.TaskStatus;
import com.interview.crm.task.repository.TaskRepository;
import com.interview.crm.task.service.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private static final String TASK_EVENTS_TOPIC = "task-events";

    private final TaskRepository taskRepository;
    private final ContactRepository contactRepository;
    private final TaskMapper taskMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        log.info("Creating task for contactId: {}", request.contactId());

        var contact = contactRepository.findById(request.contactId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + request.contactId()));

        var task = taskMapper.toEntity(request);
        task.setContact(contact);
        task.setStatus(TaskStatus.OPEN);

        task = taskRepository.save(task);
        log.info("Task created with id: {}", task.getId());

        return taskMapper.toDto(task);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        log.debug("Fetching task by id: {}", id);
        var task = findTaskById(id);
        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskResponse> getTasksByContactId(Long contactId) {
        log.debug("Fetching tasks for contactId: {}", contactId);

        if (!contactRepository.existsById(contactId)) {
            throw new ResourceNotFoundException("Contact not found with id: " + contactId);
        }

        return taskMapper.toDtoList(taskRepository.findByContactId(contactId));
    }

    @Transactional
    @Override
    public TaskResponse updateTaskDetails(Long id, UpdateTaskRequest request) {
        log.info("Updating details for task id: {}", id);
        var task = findTaskById(id);

        taskMapper.toEntity(task, request);
        var updatedTask = taskRepository.save(task);

        return taskMapper.toDto(updatedTask);
    }

    @Transactional
    @Override
    public void deleteTask(Long id) {
        log.info("Deleting task id: {}", id);
        var task = findTaskById(id);
        taskRepository.delete(task);
    }

    @Transactional
    @Override
    public TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest request) {
        log.info("Updating status for task id: {} to {}", id, request.status());

        var task = findTaskById(id);
        var oldStatus = task.getStatus();
        var newStatus = request.status();

        if (oldStatus == newStatus) {
            log.warn("Task {} already has status {}. No update needed.", id, newStatus);
            return taskMapper.toDto(task);
        }

        task.setStatus(newStatus);
        var updatedTask = taskRepository.save(task);

        var event = new TaskStatusChangedEvent(
                updatedTask.getId(),
                updatedTask.getStatus(),
                updatedTask.getContact().getId(),
                updatedTask.getDescription(),
                EventType.TASK_STATUS_CHANGED);

        kafkaTemplate.send(TASK_EVENTS_TOPIC, event);
        log.info("Sent TaskStatusChangedEvent to topic '{}' for task id: {}", TASK_EVENTS_TOPIC, id);

        return taskMapper.toDto(updatedTask);
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }
}
