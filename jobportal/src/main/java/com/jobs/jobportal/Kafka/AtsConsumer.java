package com.jobs.jobportal.Kafka;
import com.jobs.jobportal.DTO.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobs.jobportal.model.AtsScore;
import com.jobs.jobportal.model.Job;
import com.jobs.jobportal.model.Resume;
import com.jobs.jobportal.repository.AtsScoreRepository;
import com.jobs.jobportal.repository.JobRepository;
import com.jobs.jobportal.repository.ResumeRepo;

@Component
public class AtsConsumer {

    private final AtsScoreRepository atsScoreRepository;

    private final ResumeRepo resumeRepo;
    private final JobRepository jobRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String atsServiceUrl = "http://localhost:8000/ats-score";
    private final KafkaProducerService kafkaProducer;
    public AtsConsumer(JobRepository jobRepository, ResumeRepo resumeRepo, AtsScoreRepository atsScoreRepository, KafkaProducerService kafkaProducer) {
        this.jobRepository = jobRepository;
        this.resumeRepo = resumeRepo;
        this.atsScoreRepository = atsScoreRepository;
        this.kafkaProducer = kafkaProducer;
    }
    @KafkaListener(topics="ats-topic", groupId = "job-ats-group")
    public void processAtsMessage(String message) {
        try {
            Long jobId = objectMapper.readValue(message, Long.class); //Java Object ⇄ JSON
            Job job = jobRepository.findById(jobId).orElseThrow();
            List<ResumeData> resumeDataList = resumeRepo.findAll().stream().
                        map(r -> {
                            ResumeData data = new ResumeData();
                            data.setUserEmail(r.getUserEmail());
                            data.setFullText(r.getResumeText());
                            return data;
                        })
                        .toList();
            AtsRequest request = new AtsRequest();
            request.setDescription(job.getDescription());
            request.setJobId(jobId);
            request.setResumes(resumeDataList);
            List<Resume> resumes = resumeRepo.findAll(); 
            Map<String, String> emailToUsername = resumes.stream()
            .collect(Collectors.toMap(Resume::getUserEmail, Resume::getUserName));
            AtsScoreResponse[] scores = restTemplate.postForEntity(atsServiceUrl, request, AtsScoreResponse[].class).getBody();
            List<String> selectedUsers = new ArrayList<>();
            if(scores != null) {
                for(AtsScoreResponse score : scores) {
                        AtsScore atsScore = new AtsScore();
                        atsScore.setJobId(jobId);
                        atsScore.setUserEmail(score.getUserEmail());
                        atsScore.setUserName(emailToUsername.get(score.getUserEmail()));
                        atsScore.setAtsScore(score.getAtsScore());
                        atsScoreRepository.save(atsScore);
                        System.out.println("User: " + score.getUserEmail() + " ATS score: " + score.getAtsScore());
                        if(score.getAtsScore() >= 90) {
                            selectedUsers.add(score.getUserEmail());
                        }
                }
            }
            if(!selectedUsers.isEmpty())
            kafkaProducer.sendMessage("mail-topic", objectMapper.writeValueAsString(new MailRequest(jobId, selectedUsers)));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
