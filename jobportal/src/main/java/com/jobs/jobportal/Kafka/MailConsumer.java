package com.jobs.jobportal.Kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobs.jobportal.DTO.MailRequest;
import com.jobs.jobportal.service.MailService;

@Component
public class MailConsumer {
    private final MailService mailService;
    private final ObjectMapper objectMapper;
    public MailConsumer(MailService mailService, ObjectMapper objectMapper) {
        this.mailService = mailService;
        this.objectMapper = objectMapper;
    }
    @KafkaListener(topics = "mail-topic", groupId = "mail-group")
    public void processMailMessage(String message) {
        try {
            MailRequest request = objectMapper.readValue(message, MailRequest.class);
            for(String email : request.getSelectedUsers()) {
                mailService.sendEmail(email, "You can apply for the job :" + request.getJobId());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
