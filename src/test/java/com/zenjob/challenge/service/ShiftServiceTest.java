package com.zenjob.challenge.service;

import com.zenjob.challenge.entity.Job;
import com.zenjob.challenge.entity.Shift;
import com.zenjob.challenge.repository.JobRepository;
import com.zenjob.challenge.repository.ShiftRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class ShiftServiceTest {

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private JobRepository jobRepository;

    @Test
    @Transactional
    public void testCancelShift() {
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

        // Cancel one shift
        shiftService.cancelShift(shift1.getId());

        // Verify the shift is cancelled
        Optional<Shift> cancelledShift = shiftRepository.findById(shift1.getId());
        assertFalse(cancelledShift.isPresent(), "Shift should be cancelled");
    }
}
