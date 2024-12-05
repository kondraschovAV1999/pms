package org.prison.app.service;

import org.prison.model.data.edu.Course;
import org.prison.model.data.edu.Enrollment;

import java.util.List;

public interface CourseService {
    Course createCourse(Course course);
    void deleteCourse(int id);
    void updateCourse(int id, Course course);
    Course getCourseById(int id);
    List<Course> getAllCourses(int page, int size);
    void assignCourseToPrisoner(int courseId, int prisonerId);
    void deleteCourseFromPrisoner(int courseId, int prisonerId);
    void updateCourseOfPrisoner(Enrollment enrollment);
    Course findCourse(int prisonerId, int courseId);
}
