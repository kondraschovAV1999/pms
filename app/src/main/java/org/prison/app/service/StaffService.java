package org.prison.app.service;

import org.prison.model.staffs.Assessment;
import org.prison.model.staffs.Assignment;
import org.prison.model.staffs.Duty;
import org.prison.model.staffs.Staff;
import org.springframework.data.util.Pair;

import java.util.List;

public interface StaffService {
    Staff findById(int id);
    Staff save(Staff staff);
    void deleteById(int id);
    List<Duty> findAllDuties(int id, int page, int size);
    List<Assignment> findAllAssignments(int id, int page, int size);
    Pair<Staff, Assignment> createAssignment(int id, Assignment assignment);
    Staff deleteAssignment(int id, Assignment assignment);
    List<Staff> findAll( int page, int size);
    List<Assessment> findAllAssessments(int id, int page, int size);
    Pair<Staff, Assessment> createAssessment(int id, Assessment assessment);
    Staff deleteAssessment(int id, Assessment assessment);
    Staff findSupervisor(int id);
    List<Staff> findAllSupervisees(int id, int page, int size);
    Staff assignSupervisor(int id, int supervisorId);
    Staff deleteSupervisor(int id);
    Staff addSupervisee(int id, int  superviseeId);
    Staff deleteSupervisee(int id, int  superviseeId);
}
