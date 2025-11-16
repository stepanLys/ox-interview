package com.interview.crm.task.service;

import java.time.LocalDate;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.interview.crm.task.repository.TaskRepository;
import com.interview.util.events.dto.EventType;
import com.interview.util.events.dto.TaskDeadlineEvent;
import com.interview.util.events.dto.TaskStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskDeadlineScanner {

    private static final String DEADLINE_TOPIC = "deadline-events";

    private final TaskRepository taskRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedRate = 200000)
    // @Scheduled(cron = "0 0 1 * * ?") 
    public void checkTaskDeadlines() {
        log.info("Scanning for task deadlines...");
        
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        
        var tasksDueTomorrow = taskRepository.findByStatusNotAndDueDate(
            TaskStatus.COMPLETED, 
            tomorrow
        );

        log.info("Found {} tasks due tomorrow.", tasksDueTomorrow.size());

        for (var task : tasksDueTomorrow) {
            var event = new TaskDeadlineEvent(
                task.getId(),
                task.getDescription(),
                task.getDueDate(),
                task.getContact().getId(),
                "Task is due tomorrow!",
                EventType.TASK_DEADLINE
            );
            
            kafkaTemplate.send(DEADLINE_TOPIC, event);
        }
    }
}