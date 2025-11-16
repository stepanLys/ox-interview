package com.interview.crm.task.service;

import java.util.List;

import com.interview.crm.task.dto.CreateTaskRequest;
import com.interview.crm.task.dto.TaskResponse;
import com.interview.crm.task.dto.UpdateTaskRequest;
import com.interview.crm.task.dto.UpdateTaskStatusRequest;

public interface TaskService {
    TaskResponse createTask(CreateTaskRequest request);

    TaskResponse getTaskById(Long id);

    List<TaskResponse> getTasksByContactId(Long contactId);

    TaskResponse updateTaskDetails(Long id, UpdateTaskRequest request);

    void deleteTask(Long id);

    TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest request);
}
