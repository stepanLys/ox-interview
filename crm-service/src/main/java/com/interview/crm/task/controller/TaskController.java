package com.interview.crm.task.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.interview.crm.task.dto.CreateTaskRequest;
import com.interview.crm.task.dto.TaskResponse;
import com.interview.crm.task.dto.UpdateTaskRequest;
import com.interview.crm.task.dto.UpdateTaskStatusRequest;
import com.interview.crm.task.service.impl.TaskServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management operations")
public class TaskController {

    private final TaskServiceImpl taskService;

    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        var response = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get task by ID", description = "Retrieves a task by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<TaskResponse> getTaskById(@Parameter(description = "Task ID") @PathVariable Long id) {
        var response = taskService.getTaskById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("contact/{contactId}")
    @Operation(summary = "Get tasks by contact ID", description = "Retrieves all tasks associated with a specific contact")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    public ResponseEntity<List<TaskResponse>> getTasksByContactId(@Parameter(description = "Contact ID") @PathVariable Long contactId) {
        var response = taskService.getTasksByContactId(contactId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update task details", description = "Updates the details of an existing task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<TaskResponse> updateTaskDetails(@Parameter(description = "Task ID") @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        var response = taskService.updateTaskDetails(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{id}/status")
    @Operation(summary = "Update task status", description = "Updates the status of an existing task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<TaskResponse> updateTaskStatus(@Parameter(description = "Task ID") @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequest request) {
        var response = taskService.updateTaskStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete task", description = "Deletes a task by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        taskService.deleteTask(id);
    }
}