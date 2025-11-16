package com.interview.crm.task.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.interview.crm.contact.model.Contact;
import com.interview.crm.contact.repository.ContactRepository;
import com.interview.crm.task.dto.UpdateTaskStatusRequest;
import com.interview.crm.task.mapper.TaskMapper;
import com.interview.crm.task.model.Task;
import com.interview.crm.task.repository.TaskRepository;
import com.interview.crm.task.service.impl.TaskServiceImpl;
import com.interview.util.events.dto.EventType;
import com.interview.util.events.dto.TaskStatus;
import com.interview.util.events.dto.TaskStatusChangedEvent;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testUpdateTaskStatus_ShouldSendKafkaEvent() {
        var contact = new Contact();
        contact.setId(1L);

        var task = new Task();
        task.setId(1L);
        task.setStatus(TaskStatus.OPEN);
        task.setDescription("Test Task");
        task.setContact(contact);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        ArgumentCaptor<TaskStatusChangedEvent> eventCaptor = ArgumentCaptor.forClass(TaskStatusChangedEvent.class);

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest(TaskStatus.IN_PROGRESS);
        taskService.updateTaskStatus(1L, request);

        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        verify(taskRepository, times(1)).save(task);
        verify(kafkaTemplate, times(1))
                .send(eq("task-events"), eventCaptor.capture());

        TaskStatusChangedEvent sentEvent = eventCaptor.getValue();
        assertEquals(1L, sentEvent.taskId());
        assertEquals(TaskStatus.IN_PROGRESS, sentEvent.newStatus());
        assertEquals("Test Task", sentEvent.taskDescription());
        assertEquals(EventType.TASK_STATUS_CHANGED, sentEvent.eventType());
    }
}