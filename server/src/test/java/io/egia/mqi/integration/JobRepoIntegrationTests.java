package io.egia.mqi.integration;

import io.egia.mqi.job.Job;
import io.egia.mqi.job.JobRepo;
import io.egia.mqi.job.JobStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JobRepoIntegrationTests {

    @Autowired
    private JobRepo jobRepo;

    @Before
    public void setUp() {
        for(int i = 5; i > 0; i--) {
            Job job = new Job();
            job.setJobStatus(JobStatus.PENDING);
            jobRepo.saveAndFlush(job);
        }
    }

    @Test
    public void updateJobStatus() {
        jobRepo.updateJobStatus(1L, JobStatus.RUNNING);
        assertThat(jobRepo.findById(1L).get().getJobStatus()).isEqualTo(JobStatus.RUNNING);
    }

    @After
    public void tearDown() {
        List<Job> jobs = jobRepo.findAll();
        for(Job j: jobs) {
            jobRepo.delete(j);
        }
    }
}