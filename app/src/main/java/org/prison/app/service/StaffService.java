package org.prison.app.service;

import org.prison.model.staffs.Assessment;
import org.prison.model.staffs.Assignment;
import org.prison.model.staffs.Duty;
import org.prison.model.staffs.Staff;

import java.util.List;

public interface StaffService {
    Staff findById(int id);
    Staff save(Staff staff);
    void deleteById(int id);
    List<Duty> findAllDuties(int id, int page, int size);
    List<Assignment> findAllAssignments(int id, int page, int size);
    void createAssignment(int id, Assignment assignment);
    void deleteAssignment(int id, Assignment assignment);
    List<Staff> findAll( int page, int size);
    List<Assessment> findAllAssessments(int id, int page, int size);
    void createAssessment(int id, Assessment assessment);
    void deleteAssessment(int id, Assessment assessment);
    Staff findSupervisor(int id);
    List<Staff> findAllSupervisees(int id, int page, int size);
    void assignSupervisor(int id, Staff supervisor);
    void deleteSupervisor(int id, Staff supervisor);
    void addSupervisee(int id, Staff staff);
    void deleteSupervisee(int id, Staff staff);
}
