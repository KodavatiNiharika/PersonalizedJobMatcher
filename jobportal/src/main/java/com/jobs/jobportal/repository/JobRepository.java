package com.jobs.jobportal.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jobs.jobportal.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByExpiredAtAfter(LocalDateTime now);
    void deleteByExpiredAtBefore(LocalDateTime now);
     List<Job> findByUserName(String userName);

}
