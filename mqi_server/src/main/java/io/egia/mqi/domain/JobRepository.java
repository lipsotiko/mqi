package io.egia.mqi.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
	
	List<Job> findByJobName(String jobName);
	
	List<Job> findByStatusOrderByOrderIdAsc(String status);
	
	@Modifying
	@Transactional
	@Query(value="update Job j set j.status = ?2 where j.jobId = ?1")
	public void updateJobStatus(Long jobId, String status);
	
}