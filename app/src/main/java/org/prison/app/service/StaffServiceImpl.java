package org.prison.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prison.app.exceptions.NotFoundException;
import org.prison.model.data.prisoners.Prisoner;
import org.prison.model.data.staffs.Assessment;
import org.prison.model.data.staffs.Assignment;
import org.prison.model.data.staffs.Duty;
import org.prison.model.data.staffs.Staff;
import org.prison.model.repositories.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PrisonerRepository prisonerRepository;

    private static final String STAFF_NOT_FOUND = "Staff with id=%d Not Found";
    private static final String ASSIGNMENT_NOT_FOUND = "Assignment with id=%d Not Found";
    private static final String ASSESSMENT_NOT_FOUND = "Assessment with id=%d Not Found";
    public static final String PRISONER_NOT_FOUND = "Prisoner with id=%d Not Found";

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
    @Transactional
    public Staff save(int deptId, Staff staff) {
        staff = staffRepository.save(staff);
        staffRepository.assignStaffToDepartment(deptId, staff.getId());
        return staff;
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
    @Transactional
    public void assignAssignment(int id, int assignmentId) {
        Staff staff = findById(id);
        Assignment assignment = findAssignmentById(assignmentId);
        staff.getAssignments().add(assignment);
        staffRepository.save(staff);
    }

    @Override
    @Transactional
    public Staff deleteAssignment(int id, int assignmentId) {
        Staff staff = findById(id);
        staff.getAssignments().remove(findAssignmentById(assignmentId));
        staffRepository.save(staff);
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
    @Transactional
    public void createAssessment(int id, int assessmentId) {
        Staff staff = findById(id);
        Assessment assessment = findAssessmentById(assessmentId);
        staff.getAssessments().add(assessment);
        staffRepository.save(staff);
    }

    @Override
    @Transactional
    public Staff deleteAssessment(int id, int assessmentId) {
        Staff staff = findById(id);
        staff.getAssessments().remove(findAssessmentById(assessmentId));
        return staffRepository.save(staff);
    }

    @Override
    public Staff findSupervisor(int id) {
        return staffRepository.findSupervisor(id).orElseThrow(() -> {
                    String message = STAFF_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                }
        );
    }

    @Override
    public List<Staff> findAllSupervisees(int id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Staff> slice = staffRepository.findAllSupervisee(id, pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    @Transactional
    public Staff deleteSupervisor(int id) {
        Staff supervisor = findSupervisor(id);
        Staff staff = findById(id);
        supervisor.getSupervisee().remove(staff);
        return staffRepository.save(staff);
    }

    @Override
    @Transactional
    public Staff addSupervisee(int id, int staffId) {
        Staff staff = findById(id);
        Staff supervisee = findById(staffId);
        staff.getSupervisee().add(supervisee);
        return staffRepository.save(staff);
    }

    @Override
    @Transactional
    public Staff deleteSupervisee(int id, int staffId) {
        Staff staff = findById(id);
        Staff supervisee = findById(staffId);
        staff.getSupervisee().remove(supervisee);
        return staffRepository.save(staff);
    }

    @Override
    public List<Prisoner> findAllPrisoners(int id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Prisoner> slice = prisonerRepository.findAllByRespStaffId(id, pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    @Transactional
    public void assignPrisoner(int id, int prisonerId) {
        existenceCheck(id, prisonerId);
        prisonerRepository.assignPrisonerToStaff(id, prisonerId);
    }

    @Override
    @Transactional
    public void removePrisoner(int id, int prisonerId) {
        existenceCheck(id, prisonerId);
        prisonerRepository.removePrisonerFromStaff(id, prisonerId);
    }

    private Prisoner findPrisonerById(int id) {
        return prisonerRepository.findById(id)
                .orElseThrow(() -> {
                    String message = PRISONER_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                });
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

    private void existenceCheck(int id, int prisonerId) {
        if (!staffRepository.existsById(id)) {
            String message = STAFF_NOT_FOUND.formatted(id);
            log.error(message);
            throw new NotFoundException(message);
        }

        if (!prisonerRepository.existsById(prisonerId)) {
            String message = PRISONER_NOT_FOUND.formatted(id);
            log.error(message);
            throw new NotFoundException(message);
        }
    }
}
