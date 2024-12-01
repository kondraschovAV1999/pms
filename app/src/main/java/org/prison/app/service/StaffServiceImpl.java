package org.prison.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prison.app.exceptions.NotFoundException;
import org.prison.model.staffs.Assessment;
import org.prison.model.staffs.Assignment;
import org.prison.model.staffs.Duty;
import org.prison.model.staffs.Staff;
import org.prison.repositories.AssessmentRepository;
import org.prison.repositories.AssignmentRepository;
import org.prison.repositories.DutyRepository;
import org.prison.repositories.StaffRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public void createAssignment(int id, Assignment assignment) {
        findById(id).getAssignments().add(assignment);
    }

    @Override
    public void deleteAssignment(int id, Assignment assignment) {
        findById(id).getAssignments().remove(assignment);
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
    public void createAssessment(int id, Assessment assessment) {
        findById(id).addAssessment(assessment);
    }

    @Override
    public void deleteAssessment(int id, Assessment assessment) {
        findById(id).removeAssessment(assessment);
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
    public void assignSupervisor(int id, Staff supervisor) {
        supervisor.addSupervisee(findById(id));
    }

    @Override
    public void deleteSupervisor(int id, Staff supervisor) {
        supervisor.removeSupervisee(findById(id));
    }

    @Override
    public void addSupervisee(int id, Staff staff) {
        findById(id).addSupervisee(staff);
    }

    @Override
    public void deleteSupervisee(int id, Staff staff) {
        findById(id).removeSupervisee(staff);
    }
}
