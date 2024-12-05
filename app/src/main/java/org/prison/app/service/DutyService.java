package org.prison.app.service;


import org.prison.model.data.staffs.Duty;
import org.prison.model.data.utils.ValidationResp;

import java.time.LocalDate;
import java.util.List;


public interface DutyService {
    List<Duty> getDutyList(LocalDate date, int page, int size);

    ValidationResp validateDutyList(LocalDate date);

    Duty saveDuty(Duty duty);

    Duty saveDuty(int deptId, Duty duty);

    void deleteDuty(int id);

    Duty findById(int id);
}
