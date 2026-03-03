package com.jobs.jobportal.repository;
import com.jobs.jobportal.model.AtsScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AtsScoreRepository extends JpaRepository<AtsScore, Long> {

    @Query("SELECT a.jobId FROM AtsScore a WHERE a.email = :email AND a.atsScore > 40")
List<Long> findJobIdsWithHighAts(@Param("email") String email);

}
