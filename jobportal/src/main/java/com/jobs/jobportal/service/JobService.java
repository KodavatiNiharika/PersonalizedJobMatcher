package com.jobs.jobportal.service;
import java.time.LocalDateTime;
import java.util.List;

// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobs.jobportal.Kafka.KafkaProducerService;
// import com.jobs.jobportal.DTO.AtsRequest;
// import com.jobs.jobportal.DTO.AtsScoreResponse;
// import com.jobs.jobportal.DTO.ResumeData;
// import com.jobs.jobportal.model.AtsScore;
import com.jobs.jobportal.model.Job;
// import com.jobs.jobportal.model.Resume;
// import com.jobs.jobportal.repository.AtsScoreRepository;
import com.jobs.jobportal.repository.JobRepository;
// import com.jobs.jobportal.repository.ResumeRepo;
@Service
@Transactional
@EnableAsync
public class JobService {

    private final JobRepository jobRepository;
    // private final ResumeRepo resumeRepo;
    // private final AtsScoreRepository atsScoreRepo;
    private final KafkaProducerService kafkaProducer;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public JobService(JobRepository jobRepository, KafkaProducerService kafkaProducer ) {
        this.jobRepository = jobRepository;
        this.kafkaProducer = kafkaProducer;
    }
    
    // @Async
    // public void callAtsService(Job job) {

    //     RestTemplate restTemplate = new RestTemplate();
    //     String atsServiceUrl = "http://localhost:8000/ats-score";

    //     // Fetch all resumes
    //     List<Resume> resumes = resumeRepo.findAll();

    //     // Convert to ResumeData list
    //     List<ResumeData> resumeDataList = resumes.stream()
    //     .map(resume -> {
    //         ResumeData data = new ResumeData();
    //         data.setUserName(resume.getUserName());
    //         data.setFullText(resume.getResumeText());
    //         return data;
    //     })
    //     .toList();

    //     AtsRequest requestBody = new AtsRequest();
    //     requestBody.setJobId(job.getId());
    //     requestBody.setDescription(job.getDescription());
    //     requestBody.setResumes(resumeDataList);
    //         System.out.println("Calling ATS service for job: " + job.getId());
    //     try {
    // ResponseEntity<AtsScoreResponse[]> response =
    //     restTemplate.postForEntity(
    //         atsServiceUrl,
    //         requestBody,
    //         AtsScoreResponse[].class
    //     );

    //     AtsScoreResponse[] scores = response.getBody();
    //     System.out.println("Response from ATS: " + (scores != null ? scores.length : "null"));

    //     if (scores != null) {
    //         for (AtsScoreResponse score : scores) {
    //             System.out.println("Saving score: " + score.getUserName() + " -> " + score.getAtsScore());
    //             AtsScore atsScore = new AtsScore();
    //             atsScore.setJobId(job.getId());
    //             atsScore.setUserName(score.getUserName());
    //             atsScore.setAtsScore(score.getAtsScore());
    //             atsScoreRepo.save(atsScore);
    //         }
    //     }
    // } catch (Exception e) {
    //     e.printStackTrace();
    //     System.out.println("Failed to call ATS service: " + e.getMessage());
    // }
    // }

    public Job createJob(Job job, String loggedInUser) {
        job.setUserName(loggedInUser);
        job.setCreatedAt(LocalDateTime.now());
        job.setExpiredAt(LocalDateTime.now().plusDays(7));
        Job savedJob = jobRepository.save(job);
        try {
            kafkaProducer.sendMessage("ats-topic", objectMapper.writeValueAsString(savedJob.getId()));
        } catch(Exception e) {
            e.printStackTrace();
        }

        return savedJob;
    }

    public Job getJobById(Long id) {
    return jobRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Job not found"));
    }



    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getJobsByUser(String userName) {
        return jobRepository.findByUserName(userName);
    }

    public Job updateJob(Long id, Job updatedJob, String loggedInUser) {

        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!existingJob.getUserName().equals(loggedInUser)) {
            throw new RuntimeException("Unauthorized");
        }

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setCompany(updatedJob.getCompany());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setApplyLink(updatedJob.getApplyLink());

        return jobRepository.save(existingJob);
    }

    public void deleteJob(Long id, String loggedInUser) {

    Job existingJob = jobRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Job not found"));

    if (!existingJob.getUserName().equals(loggedInUser)) {
        throw new RuntimeException("Unauthorized");
    }

    jobRepository.delete(existingJob);
}

}
