package com.zenjob.challenge.controller;

import com.zenjob.challenge.dto.ResponseDto;
import com.zenjob.challenge.entity.Job;
import com.zenjob.challenge.service.JobService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping(path = "/job")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping
    @ResponseBody
    public ResponseDto<RequestJobResponse> requestJob(@RequestBody @Valid RequestJobRequestDto dto) {
        Job job = jobService.createJob(UUID.randomUUID(), dto.start, dto.end);
        return ResponseDto.<RequestJobResponse>builder()
                .data(RequestJobResponse.builder()
                        .jobId(job.getId())
                        .build())
                .build();
    }

    @DeleteMapping("/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelJob(@PathVariable UUID jobId) {
        jobService.cancelJob(jobId);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class RequestJobRequestDto {
        @NotNull
        private UUID      companyId;
        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate start;
        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate   end;
    }

    @Builder
    @Data
    private static class RequestJobResponse {
        UUID jobId;
    }
}
