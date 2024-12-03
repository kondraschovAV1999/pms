package org.prison.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prison.app.exceptions.NotFoundException;
import org.prison.model.data.staffs.Assessment;
import org.prison.model.data.staffs.Assignment;
import org.prison.model.data.staffs.Duty;
import org.prison.model.data.staffs.Staff;
import org.prison.model.repositories.AssessmentRepository;
import org.prison.model.repositories.AssignmentRepository;
import org.prison.model.repositories.DutyRepository;
import org.prison.model.repositories.StaffRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssessmentRepository assessmentRepository;
    private final DutyRepository dutyRepository;

    private static final String STAFF_NOT_FOUND = "Staff with id=%d Not Found";
    private static final String ASSIGNMENT_NOT_FOUND = "Assignment with id=%d Not Found";
    private static final String ASSESSMENT_NOT_FOUND = "Assessment with id=%d Not Found";


    @Override
    public Staff findById(int id) {
        return staffRepository.findById(id).orElseThrow(() -> {
                    String message = STAFF_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                }
        );
    }

    @Override
    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public void deleteById(int id) {
        staffRepository.deleteById(id);
    }

    @Override
    public List<Duty> findAllDuties(int id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Duty> slice = dutyRepository.findAllByStaffId(id, pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    public List<Assignment> findAllAssignments(int id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Assignment> slice = assignmentRepository.findAllByStaffId(id, pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    public Pair<Staff, Assignment> createAssignment(int id, Assignment assignment) {
        Staff staff = findById(id);
        staff.getAssignments().add(assignment);
        assignmentRepository.save(assignment);
        return Pair.of(staff, assignment);
    }

    @Override
    public Staff deleteAssignment(int id, int assignmentId) {
        Staff staff = findById(id);
        staff.getAssignments().remove(findAssignmentById(assignmentId));
        return staff;
    }

    @Override
    public List<Staff> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Staff> slice = staffRepository.findAll(pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    public List<Assessment> findAllAssessments(int id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Assessment> slice = assessmentRepository.findAllByStaffId(id, pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    public Pair<Staff, Assessment> createAssessment(int id, Assessment assessment) {
        Staff staff = findById(id);
        staff.addAssessment(assessment);
        assessmentRepository.save(assessment);
        return Pair.of(staff, assessment);
    }

    @Override
    public Staff deleteAssessment(int id, int assessmentId) {
        Staff staff = findById(id);
        staff.removeAssessment(findAssessmentById(assessmentId));
        return staff;
    }

    @Override
    public Staff findSupervisor(int id) {
        return staffRepository.findSupervisor(id).orElse(new Staff());
    }

    @Override
    public List<Staff> findAllSupervisees(int id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Staff> slice = staffRepository.findAllSupervisee(id, pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    public Staff assignSupervisor(int id, int supervisorId) {
        Staff supervisor = findById(supervisorId);
        Staff staff = findById(id);
        supervisor.addSupervisee(staff);
        staff.setMaster(supervisor);
        return staff;
    }

    @Override
    public Staff deleteSupervisor(int id) {
        Staff supervisor = findSupervisor(id);
        Staff staff = findById(id);
        if (supervisor != null) {
            supervisor.removeSupervisee(staff);
        }
        return staff;
    }

    @Override
    public Staff addSupervisee(int id, int staffId) {
        Staff staff = findById(id);
        Staff supervisee = findById(staffId);
        staff.addSupervisee(supervisee);
        return staff;
    }

    @Override
    public Staff deleteSupervisee(int id, int staffId) {
        Staff staff = findById(id);
        Staff supervisee = findById(staffId);
        staff.removeSupervisee(supervisee);
        return staff;
    }

    private Assignment findAssignmentById(int id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> {
                    String message = ASSIGNMENT_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                });
    }

    private Assessment findAssessmentById(int id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> {
                    String message = ASSESSMENT_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                });
    }
}
