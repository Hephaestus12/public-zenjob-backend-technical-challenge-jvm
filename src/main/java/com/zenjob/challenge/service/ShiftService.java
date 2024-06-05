package com.zenjob.challenge.service;

import com.zenjob.challenge.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShiftService {

    private final ShiftRepository shiftRepository;

    public void cancelShift(UUID shiftId) {
        shiftRepository.deleteById(shiftId);
    }
}
