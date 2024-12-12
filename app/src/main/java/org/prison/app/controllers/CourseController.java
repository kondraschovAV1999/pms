package org.prison.app.controllers;

import lombok.AllArgsConstructor;
import org.prison.app.service.CourseService;
import org.prison.model.data.edu.Course;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/courses")
//@CrossOrigin(origins = "*", allowCredentials = "false")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable int id) {
        return courseService.getCourseById(id);
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
    }

    @PostMapping("/{id}")
    public void updateCourse(@PathVariable int id,
                             @RequestBody Course course) {
        courseService.updateCourse(id, course);
    }

    @GetMapping
    public List<Course> getAllCourses(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return courseService.getAllCourses(page, size);
    }

}
