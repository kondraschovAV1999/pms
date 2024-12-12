package org.prison.app.controllers;

import lombok.AllArgsConstructor;
import org.prison.app.service.StaffService;
import org.prison.model.data.prisoners.Prisoner;
import org.prison.model.data.staffs.Assessment;
import org.prison.model.data.staffs.Assignment;
import org.prison.model.data.staffs.Duty;
import org.prison.model.data.staffs.Staff;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping
//@CrossOrigin(origins = "*", allowCredentials = "false")
public class StaffController {

    private final StaffService staffService;

    @GetMapping("/staff/{id}")
    public Staff findById(@PathVariable int id) {
        return staffService.findById(id);
    }

    @PostMapping("departments/{deptId}/staff")
    public Staff save(@PathVariable int deptId,
                      @RequestBody Staff staff) {
        return staffService.save(deptId, staff);
    }

    @DeleteMapping("/staff/{id}")
    public void deleteById(@PathVariable int id) {
        //TODO FIX INTEGRITY CONSTRAINT
        staffService.deleteById(id);
    }

    @GetMapping("/staff/{id}/duties")
    public List<Duty> findAllDuties(@PathVariable int id,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return staffService.findAllDuties(id, page, size);
    }

    @GetMapping("/staff/{id}/assignments")
    public List<Assignment> findAllAssignments(@PathVariable int id,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return staffService.findAllAssignments(id, page, size);
    }

    @PostMapping("/staff/{id}/assignments/{assignmentId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addAssignment(@PathVariable int id,
                              @PathVariable int assignmentId) {
        staffService.assignAssignment(id, assignmentId);
    }

    @DeleteMapping("/staff/{id}/assignments/{assignmentId}")
    public void removeAssignment(@PathVariable int id,
                                 @PathVariable int assignmentId) {
        staffService.deleteAssignment(id, assignmentId);
    }

    @GetMapping("/staff/{id}/assessments")
    public List<Assessment> findAllAssessments(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return staffService.findAllAssessments(id, page, size);
    }

    @PostMapping("/staff/{id}/assessments/{assessmentId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addAssessment(@PathVariable int id,
                              @PathVariable int assessmentId) {
        staffService.createAssessment(id, assessmentId);
    }

    @DeleteMapping("/staff/{id}/assessments/{assessmentId}")
    public void removeAssessment(
            @PathVariable int id,
            @PathVariable int assessmentId) {
        staffService.deleteAssessment(id, assessmentId);
    }

    @GetMapping("/staff/{id}/supervisor")
    public Staff findSupervisor(@PathVariable int id) {
        return staffService.findSupervisor(id);
    }

    @GetMapping("/staff/{id}/supervisees")
    public List<Staff> findAllSupervisees(@PathVariable int id,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return staffService.findAllSupervisees(id, page, size);
    }

    @DeleteMapping("/staff/{id}/supervisor")
    public void deleteSupervisor(@PathVariable int id) {
        staffService.deleteSupervisor(id);
    }

    @PostMapping("/staff/{id}/supervisees/{superviseeId}")
    public void addSupervisee(@PathVariable int id,
                              @PathVariable int superviseeId) {
        staffService.addSupervisee(id, superviseeId);
    }


    @DeleteMapping("/staff/{id}/supervisees/{superviseeId}")
    public void deleteSupervisee(@PathVariable int id,
                                 @PathVariable int superviseeId) {
        staffService.deleteSupervisee(id, superviseeId);
    }

    @GetMapping("/staff")
    public List<Staff> findAll(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        return staffService.findAll(page, size);
    }

    @PostMapping("/staff/{id}/prisoners/{prisonerId}")
    public void assignPrisoner(@PathVariable int id,
                               @PathVariable int prisonerId) {
        staffService.assignPrisoner(id, prisonerId);
    }

    @DeleteMapping("/staff/{id}/prisoners/{prisonerId}")
    public void removePrisoner(@PathVariable int id,
                               @PathVariable int prisonerId) {
        staffService.removePrisoner(id, prisonerId);
    }

    @GetMapping("/staff/{id}/prisoners")
    public List<Prisoner> getPrisoners(@PathVariable int id,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return staffService.findAllPrisoners(id, page, size);
    }
}
