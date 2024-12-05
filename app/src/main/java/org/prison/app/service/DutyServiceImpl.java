package org.prison.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prison.app.exceptions.NotFoundException;
import org.prison.model.data.staffs.Duty;
import org.prison.model.data.utils.StaffLevel;
import org.prison.model.data.utils.ValidationResp;
import org.prison.model.repositories.DutyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class DutyServiceImpl implements DutyService {

    private DutyRepository dutyRepository;

    private static final String DUTY_NOT_FOUND = "Duty with id=%d Not Found";

    @Override
    public List<Duty> getDutyList(LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Duty> slice = dutyRepository.findAllByDate(date, pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    public ValidationResp validateDutyList(LocalDate date) {
        List<Duty> invalidDuties = dutyRepository.findAllByDate(date).stream()
                .filter(d -> d.getStaffs().stream()
                        .allMatch(s -> s.getLevel() == StaffLevel.NORMAL_STAFF))
                .toList();
        return new ValidationResp(invalidDuties, invalidDuties.isEmpty());
    }

    @Override
    public Duty saveDuty(Duty duty) {
        return dutyRepository.save(duty);
    }

    @Override
    @Transactional
    public Duty saveDuty(int deptId, Duty duty) {
        duty = dutyRepository.save(duty);
        dutyRepository.assignDutyToDepartment(deptId, duty.getId());
        return duty;
    }

    @Override
    public void deleteDuty(int id) {
        dutyRepository.deleteById(id);
    }

    @Override
    public Duty findById(int id) {
        return dutyRepository.findById(id).orElseThrow(() -> {
                    String message = DUTY_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                }
        );
    }
}
