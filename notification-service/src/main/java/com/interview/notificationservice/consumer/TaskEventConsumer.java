package com.interview.notificationservice.consumer;

import com.interview.util.events.dto.TaskDeadlineEvent;
import com.interview.util.events.dto.TaskStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEventConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
        topics = "task-events", 
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleTaskStatusChange(@Payload TaskStatusChangedEvent event) {
        log.info("Received event from Kafka: Task {} changed status to {}", 
            event.taskId(), event.newStatus());

        messagingTemplate.convertAndSend("/topic/notifications", event);
    }

    @KafkaListener(
        topics = "deadline-events",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleTaskDeadline(@Payload TaskDeadlineEvent event) {
        log.info("Received Kafka DEADLINE event: Task {} is due tomorrow", event.taskId());

        messagingTemplate.convertAndSend("/topic/notifications", event);   
    }
}
