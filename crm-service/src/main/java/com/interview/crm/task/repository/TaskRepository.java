package com.interview.crm.task.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interview.crm.task.model.Task;
import com.interview.util.events.dto.TaskStatus;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    @EntityGraph(attributePaths = {"contact"})
    List<Task> findByContactId(Long contactId);

    List<Task> findByStatusNotAndDueDate(TaskStatus status, LocalDate dueDate);
}
