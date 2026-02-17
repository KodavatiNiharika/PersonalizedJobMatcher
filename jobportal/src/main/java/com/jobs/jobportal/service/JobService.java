package com.jobs.jobportal.service;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jobs.jobportal.model.Job;
import com.jobs.jobportal.repository.JobRepository;
@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job createJob(Job job, String loggedInUser) {
        job.setUserName(loggedInUser);
        job.setCreatedAt(LocalDateTime.now());
        return jobRepository.save(job);
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
