package com.zenjob.challenge.service;

import com.zenjob.challenge.entity.Shift;
import com.zenjob.challenge.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShiftService {

    private final ShiftRepository shiftRepository;

    public void cancelShiftsForTalent(UUID talentId) {
        List<Shift> shifts = shiftRepository.findAllByTalentId(talentId);

        for (Shift shift : shifts) {
            Shift replacementShift = Shift.builder()
                    .job(shift.getJob())
                    .startTime(shift.getStartTime())
                    .endTime(shift.getEndTime())
                    .build();
            shiftRepository.save(replacementShift);
        }

        shiftRepository.deleteAllByTalentId(talentId);
    }
}
