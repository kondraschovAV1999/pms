package org.prison.app.controllers;

import lombok.AllArgsConstructor;
import org.prison.app.service.DegreeService;
import org.prison.model.data.edu.Course;
import org.prison.model.data.edu.Degree;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/degrees")
public class DegreeController {
    private final DegreeService degreeService;

    @GetMapping("/{id}")
    public Degree getDegree(@PathVariable int id) {
        return degreeService.getDegreeById(id);
    }

    @PostMapping
    public Degree createDegree(@RequestBody Degree degree) {
        return degreeService.createDegree(degree);
    }

    @DeleteMapping("/{id}")
    public void deleteDegree(@PathVariable int id) {
        degreeService.deleteDegree(id);
    }

    @PostMapping("/{id}")
    public void updateDegree(@PathVariable int id,
                             @RequestBody Degree degree) {
        degreeService.updateDegree(id, degree);
    }

    @GetMapping
    public List<Degree> getAllDegrees(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return degreeService.getAllDegrees(page, size);
    }

    @PostMapping("{degreeId}/courses/{courseId}")
    public void addCourse(@PathVariable int degreeId,
                          @PathVariable int courseId) {
        degreeService.associateCourseToDegree(courseId, degreeId);
    }

    @DeleteMapping("{degreeId}/courses/{courseId}")
    public void removeCourse(@PathVariable int degreeId,
                             @PathVariable int courseId) {
        degreeService.removerCourseFromDegree(courseId, degreeId);
    }

    @GetMapping("{degreeId}/courses")
    public List<Course> getAllCourses(@PathVariable int degreeId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return degreeService.getAllCourses(degreeId, page, size);
    }
}
