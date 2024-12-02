package org.prison.app.controllers;

import lombok.AllArgsConstructor;
import org.prison.app.service.StaffService;
import org.prison.model.staffs.Assessment;
import org.prison.model.staffs.Assignment;
import org.prison.model.staffs.Duty;
import org.prison.model.staffs.Staff;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/pms/api/staff")
public class StaffController {

    private final StaffService staffService;

    @GetMapping("/{id}")
    public Staff findById(@PathVariable int id) {
        return staffService.findById(id);
    }

    @PostMapping
    public Staff save(@RequestBody Staff staff) {
        return staffService.save(staff);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id) {
        staffService.deleteById(id);
    }

    @GetMapping("/{id}/duties")
    public List<Duty> findAllDuties(@PathVariable int id,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return staffService.findAllDuties(id, page, size);
    }

    @GetMapping("/{id}/assignments")
    public List<Assignment> findAllAssignments(@PathVariable int id,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return staffService.findAllAssignments(id, page, size);
    }

    @PostMapping("/{id}/assignments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Assignment createAssignment(@PathVariable int id,
                                       @RequestBody Assignment assignment) {
        Pair<Staff, Assignment> pair = staffService.createAssignment(id, assignment);
        staffService.save(pair.getFirst());
        return pair.getFirst().getAssignments().stream()
                .filter(a -> a.equals(pair.getSecond()))
                .findFirst()
                .orElse(null);
    }

    @DeleteMapping("/{id}/assignments")
    public void deleteAssignment(@PathVariable int id,
                                 @RequestBody Assignment assignment) {
        staffService.save(staffService.deleteAssignment(id, assignment));
    }

    @GetMapping("/{id}/assessments")
    public List<Assessment> findAllAssessments(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return staffService.findAllAssessments(id, page, size);
    }

    @PostMapping("/{id}/assessments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Assessment createAssessment(@PathVariable int id,
                                       @RequestBody Assessment assessment) {
        Pair<Staff, Assessment> pair = staffService.createAssessment(id, assessment);
        staffService.save(pair.getFirst());
        return pair.getFirst().getAssessments().stream()
                .filter(a -> a.equals(pair.getSecond()))
                .findFirst()
                .orElse(null);
    }

    @DeleteMapping("/{id}/assessments")
    public void deleteAssessment(
            @PathVariable int id,
            @RequestBody Assessment assessment) {
        staffService.save(staffService.deleteAssessment(id, assessment));
    }

    @GetMapping("/{id}/supervisor")
    public Staff findSupervisor(@PathVariable int id) {
        return staffService.findSupervisor(id);
    }

    @GetMapping("/{id}/supervisees")
    public List<Staff> findAllSupervisees(@PathVariable int id,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return staffService.findAllSupervisees(id, page, size);
    }

    @PostMapping("/{id}/supervisor/{supervisorId}")
    public void assignSupervisor(@PathVariable int id,
                                 @PathVariable int supervisorId) {
        staffService.save(staffService.assignSupervisor(id, supervisorId));
    }

    @DeleteMapping("/{id}/supervisor")
    public void deleteSupervisor(@PathVariable int id) {
        staffService.save(staffService.deleteSupervisor(id));
    }

    @PostMapping("/{id}/supervisees/{superviseeId}")
    public void addSupervisee(@PathVariable int id,
                              @PathVariable int superviseeId) {
        staffService.save(staffService.addSupervisee(id, superviseeId));
    }


    @DeleteMapping("/{id}/supervisees/{superviseeId}")
    public void deleteSupervisee(@PathVariable int id,
                                 @PathVariable int superviseeId) {
        staffService.save(staffService.deleteSupervisee(id, superviseeId));
    }

    @GetMapping
    public List<Staff> findAll(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        return staffService.findAll(page, size);
    }
}
