package com.zenjob.challenge.service;

import com.zenjob.challenge.entity.Job;
import com.zenjob.challenge.entity.Shift;
import com.zenjob.challenge.repository.JobRepository;
import com.zenjob.challenge.repository.ShiftRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JobServiceTest {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Test
    public void testCancelJob() {
        // Create a job with shifts
        UUID jobId = UUID.randomUUID();
        Job job = Job.builder()
                .id(jobId)
                .companyId(UUID.randomUUID())
                .startTime(Instant.now().plusSeconds(3600))
                .endTime(Instant.now().plusSeconds(7200))
                .build();

        Shift shift1 = Shift.builder()
                .id(UUID.randomUUID())
                .job(job)
                .startTime(Instant.now().plusSeconds(3600))
                .endTime(Instant.now().plusSeconds(7200))
                .build();
        Shift shift2 = Shift.builder()
                .id(UUID.randomUUID())
                .job(job)
                .startTime(Instant.now().plusSeconds(10800))
                .endTime(Instant.now().plusSeconds(14400))
                .build();

        job.setShifts(Arrays.asList(shift1, shift2));

        jobRepository.save(job);
        shiftRepository.saveAll(Arrays.asList(shift1, shift2));

        // Cancel the job
        jobService.cancelJob(jobId);

        // Verify the job and its shifts are cancelled
        assertFalse(jobRepository.findById(jobId).isPresent());
        assertTrue(shiftRepository.findAllByJobId(jobId).isEmpty());
    }
}
