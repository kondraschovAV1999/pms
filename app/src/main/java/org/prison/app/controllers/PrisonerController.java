package org.prison.app.controllers;

import lombok.AllArgsConstructor;
import org.prison.app.service.CourseService;
import org.prison.app.service.DegreeService;
import org.prison.app.service.PrisonerService;
import org.prison.model.data.edu.Course;
import org.prison.model.data.edu.Degree;
import org.prison.model.data.prisoners.Communication;
import org.prison.model.data.prisoners.Prisoner;
import org.prison.model.data.utils.StatisticsReq;
import org.prison.model.data.utils.StatisticsResp;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping
//@CrossOrigin(origins = "*", allowCredentials = "false")
public class PrisonerController {

    private final PrisonerService prisonerService;
    private final CourseService courseService;
    private final DegreeService degreeService;

    @GetMapping("prisoners/statistics")
    public StatisticsResp getStatistics(@RequestBody StatisticsReq req) {
        return prisonerService.getStatistics(req);
    }

    @GetMapping("prisoners")
    public List<Prisoner> getPrisonersByKeyword(@RequestParam String keyword,
                                                @RequestParam String filter,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "0") int size) {
        return prisonerService.findPrisonersByKeyword(keyword, filter, page, size);
    }

    @GetMapping("prisoners/{id}")
    public Prisoner getPrisoner(@PathVariable int id) {
        return prisonerService.findById(id);
    }

    @GetMapping("prisoners/list")
    public List<Prisoner> getAllPrisoners(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "0") int size) {
        return prisonerService.findAllPrisoners(page, size);
    }

    @PostMapping("/departments/{deptId}/prisoners")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Prisoner createPrisoner(@PathVariable int deptId,
                                   @RequestBody Prisoner prisoner) {
        return prisonerService.savePrisoner(deptId, prisoner);
    }

    @DeleteMapping("prisoners/{id}")
    public void deletePrisoner(@PathVariable int id) {
        prisonerService.deletePrisonerById(id);
    }

    @PostMapping("/departments/{deptId}/prisoners/{id}")
    public Prisoner editPrisoner(@PathVariable int deptId,
                                 @RequestBody Prisoner prisoner,
                                 @PathVariable int id) {
        return prisonerService.editPrisoner(id, deptId, prisoner);
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
        return courseService.findCourse(prisonerId, courseId);
    }

    @PostMapping("prisoners/{prisonerId}/courses/{courseId}")
    public void addCourse(@PathVariable int prisonerId,
                          @PathVariable int courseId) {
        courseService.assignCourseToPrisoner(courseId, prisonerId);
    }

    @DeleteMapping("prisoners/{prisonerId}/courses/{courseId}")
    public void deleteCourse(@PathVariable int prisonerId,
                             @PathVariable int courseId) {
        courseService.deleteCourseFromPrisoner(courseId, prisonerId);
    }

    @GetMapping("prisoners/{id}/degrees")
    public List<Degree> getPrisonerDegrees(@PathVariable int id) {
        return prisonerService.findAllDegrees(id);
    }

    @GetMapping("prisoners/{prisonerId}/degrees/{degreeId}")
    public Degree getDegreeById(@PathVariable int prisonerId,
                                @PathVariable int degreeId) {
        return degreeService.findDegree(prisonerId, degreeId);
    }

    @PostMapping("prisoners/{prisonerId}/degrees/{degreeId}")
    public void addDegree(@PathVariable int prisonerId,
                          @PathVariable int degreeId) {
        degreeService.assignDegreeToPrisoner(degreeId, prisonerId);
    }

    @DeleteMapping("prisoners/{prisonerId}/degrees/{degreeId}")
    public void deleteDegree(@PathVariable int prisonerId,
                             @PathVariable int degreeId) {
        degreeService.deleteDegreeFromPrisoner(degreeId, prisonerId);
    }

    @GetMapping("prisoners/release7d")
    public List<Prisoner> getPrisonersRelease7d(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "0") int size) {
        return prisonerService.findReleasePrisoners(page, size);
    }

    @GetMapping("departments/{id}/prisoners")
    public List<Prisoner> getPrisonersByDeptId(@PathVariable int id,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "0") int size) {
        return prisonerService.findAllByDept(id, page, size);
    }

    @GetMapping("prisoners/{id}/communication")
    public List<Communication> getPrisonersCommunications(@PathVariable int id) {
        return prisonerService.findAllCommunications(id);
    }

    @PostMapping("prisoners/{id}/communication")
    public Communication addCommunication(@PathVariable int id,
                                          @RequestBody Communication communication) {
        return prisonerService.addCommunication(id, communication);
    }

    @DeleteMapping("prisoners/{prisonerId}/communication/{commId}")
    public void deleteCommunication(@PathVariable int prisonerId,
                                    @PathVariable int commId) {
        prisonerService.deleteCommunication(prisonerId, commId);
    }
}
