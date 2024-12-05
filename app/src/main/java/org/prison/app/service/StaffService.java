package org.prison.app.service;


import org.prison.model.data.prisoners.Prisoner;
import org.prison.model.data.staffs.Assessment;
import org.prison.model.data.staffs.Assignment;
import org.prison.model.data.staffs.Duty;
import org.prison.model.data.staffs.Staff;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StaffService {
    Staff findById(int id);

    Staff save(Staff staff);

    Staff save(int deptId, Staff staff);

    void deleteById(int id);

    List<Duty> findAllDuties(int id, int page, int size);

    List<Assignment> findAllAssignments(int id, int page, int size);

    void assignAssignment(int id, int assignmentId);

    Staff deleteAssignment(int id, int assignmentId);

    List<Staff> findAll(int page, int size);

    List<Assessment> findAllAssessments(int id, int page, int size);

    void createAssessment(int id, int assessmentId);

    Staff deleteAssessment(int id, int assessmentId);

    Staff findSupervisor(int id);

    List<Staff> findAllSupervisees(int id, int page, int size);

    Staff deleteSupervisor(int id);

    Staff addSupervisee(int id, int superviseeId);

    Staff deleteSupervisee(int id, int superviseeId);

    List<Prisoner> findAllPrisoners(int id, int page, int size);

    @Transactional
    void assignPrisoner(int id, int prisonerId);

    @Transactional
    void removePrisoner(int id, int prisonerId);
}
