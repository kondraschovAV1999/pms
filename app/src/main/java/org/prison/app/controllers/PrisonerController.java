package org.prison.app.controllers;

import lombok.AllArgsConstructor;
import org.prison.app.service.PrisonerService;
import org.prison.model.edu.Course;
import org.prison.model.edu.Degree;
import org.prison.model.edu.Enrollment;
import org.prison.model.edu.PrisonerDegree;
import org.prison.model.prisoners.Communication;
import org.prison.model.prisoners.Prisoner;
import org.prison.model.utils.StatisticsReq;
import org.prison.model.utils.StatisticsResp;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/pms/api")
public class PrisonerController {

    private final PrisonerService prisonerService;

    @GetMapping("prisoners")
    public StatisticsResp getStatistics(@RequestBody StatisticsReq req) {
        return prisonerService.getStatistics(req);
    }

    @GetMapping("prisoners")
    public List<Prisoner> getPrisonersByKeyword(@RequestParam String keyword,
                                                @RequestParam String filter,
                                                @RequestParam int page,
                                                @RequestParam int size) {
        return prisonerService.findPrisonersByKeyword(keyword, filter, page, size);
    }

    @GetMapping("prisoners/{id}")
    public Prisoner getPrisoner(@PathVariable int id) {
        return prisonerService.findById(id);
    }

    @GetMapping("prisoners/list")
    public List<Prisoner> getAllPrisoners(@RequestParam int page,
                                          @RequestParam int size) {
        return prisonerService.findAllPrisoners(page, size);
    }

    @PostMapping("prisoners")
    public Prisoner createPrisoner(@RequestBody Prisoner prisoner) {
        return prisonerService.savePrisoner(prisoner);
    }

    @DeleteMapping("prisoners/{id}")
    public void deletePrisoner(@PathVariable int id) {
        prisonerService.deletePrisonerById(id);
    }

    @PostMapping("prisoners/{id}")
    public Prisoner editPrisoner(@RequestBody Prisoner prisoner, @PathVariable int id) {
        return prisonerService.savePrisoner(
                prisonerService.editPrisoner(id, prisoner)
        );
    }

    @PutMapping("prisoners/{id}")
    public Prisoner replacePrisoner(@RequestBody Prisoner prisoner, @PathVariable int id) {
        prisoner.setId(id);
        return prisonerService.savePrisoner(prisoner);
    }

    @GetMapping("prisoners/count")
    public int getPrisonerCount() {
        return prisonerService.numberPrisoners();
    }

    @GetMapping("prisoners/{id}/courses")
    public List<Course> getPrisonerCourses(@PathVariable int id) {
        return prisonerService.findAllCourses(id);
    }

    @GetMapping("prisoners/{prisonerId}/courses/{courseId}")
    public Course getCourseById(@PathVariable int prisonerId,
                                @PathVariable int courseId) {
        return prisonerService.findCourseById(prisonerId, courseId);
    }

    @PostMapping("prisoners/{prisonerId}/courses")
    public Course addCourse(@PathVariable int prisonerId,
                            @RequestBody Course course) {
        Enrollment enrollment = prisonerService.addCourse(prisonerId, course);
        Prisoner prisoner = prisonerService.savePrisoner(enrollment.getPrisoner());
        return prisonerService.findCourseByEnrl(prisoner, enrollment);
    }

    @DeleteMapping("prisoners/{prisonerId}/courses/{courseId}")
    public void deleteCourse(@PathVariable int prisonerId,
                             @PathVariable int courseId) {
        prisonerService.savePrisoner(
                prisonerService.deleteCourse(prisonerId, courseId));
    }

    @GetMapping("prisoners/{id}/degrees")
    public List<Degree> getPrisonerDegrees(@PathVariable int id) {
        return prisonerService.findAllDegrees(id);
    }

    @GetMapping("prisoners/{prisonerId}/degrees/{degreeId}")
    public Degree getDegreeById(@PathVariable int prisonerId,
                                @PathVariable int degreeId) {
        return prisonerService.findDegreeById(prisonerId, degreeId);
    }

    @PostMapping("prisoners/{prisonerId}/degrees")
    public Degree addDegree(@PathVariable int prisonerId,
                            @RequestBody Degree degree) {
        PrisonerDegree prisonerDegree = prisonerService.addDegree(prisonerId, degree);
        Prisoner prisoner = prisonerService.savePrisoner(prisonerDegree.getPrisoner());
        return prisonerService.findDegreeByPD(prisoner, prisonerDegree);
    }

    @DeleteMapping("prisoners/{prisonerId}/degrees/{degreeId}")
    public void deleteDegree(@PathVariable int prisonerId,
                             @PathVariable int degreeId) {
        prisonerService.savePrisoner(
                prisonerService.deleteDegree(prisonerId, degreeId));
    }

    @GetMapping("prisoners/release7d")
    public List<Prisoner> getPrisonersRelease7d(@RequestParam int page,
                                                @RequestParam int size) {
        return prisonerService.findReleasePrisoners(page, size);
    }

    @GetMapping("prisoners")
    public List<Prisoner> getPrisonersByStatus(@RequestParam String status,
                                               @RequestParam int page,
                                               @RequestParam int size) {
        return prisonerService.findAllByStatus(status, page, size);
    }

    @GetMapping("department/{id}/prisoners")
    public List<Prisoner> getPrisonersByDeptId(@PathVariable int id,
                                               @RequestParam int page,
                                               @RequestParam int size) {
        return prisonerService.findAllByDept(id, page, size);
    }

    @GetMapping("staff/{id}/prisoners")
    public List<Prisoner> getPrisonersByRespStaffId(@PathVariable int id,
                                                    @RequestParam int page,
                                                    @RequestParam int size) {
        return prisonerService.findAllByRespStaff(id, page, size);
    }

    @GetMapping("prisoners/{id}/communication")
    public List<Communication> getPrisonersCommunications(@PathVariable int id) {
        return prisonerService.findAllCommunications(id);
    }

    @PostMapping("prisoners/{id}/communication")
    public Communication addCommunication(@PathVariable int id,
                                          @RequestBody Communication communication) {
        Communication savedComm = prisonerService.addCommunication(id, communication);
        return prisonerService.savePrisoner(savedComm.getPrisoner()).getCommunications()
                .stream()
                .filter(c -> c.getId().equals(savedComm.getId()))
                .findFirst()
                .orElse(null);
    }

    @DeleteMapping("prisoners/{prisonerId}/communication/{commId}")
    public void deleteCommunication(@PathVariable int prisonerId, @PathVariable int commId) {
        prisonerService.savePrisoner(
                prisonerService.deleteCommunication(prisonerId, commId));
    }
}
