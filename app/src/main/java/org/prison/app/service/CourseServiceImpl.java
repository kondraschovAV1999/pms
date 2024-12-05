package org.prison.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prison.app.exceptions.NotFoundException;
import org.prison.model.data.edu.Course;
import org.prison.model.data.edu.Enrollment;
import org.prison.model.data.utils.Status;
import org.prison.model.repositories.CourseRepository;
import org.prison.model.repositories.EnrollmentRepository;
import org.prison.model.repositories.PrisonerRepository;
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
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PrisonerRepository prisonerRepository;

    private static final String COURSE_NOT_FOUND = "Course with id=%d Not Found";
    public static final String ENROLLMENT_NOT_FOUND = "Enrollment with courseId=%d and prisonerId=%d Not Found";
    private static final String PRISONER_NOT_FOUND = "Prisoner with id=%d Not Found";

    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateCourse(int id, Course course) {
        Course oldCourse = getCourseById(id);

        if (course.getName() != null)
            oldCourse.setName(course.getName());

        if (course.getTeacher() != null)
            oldCourse.setTeacher(course.getTeacher());

        courseRepository.save(course);
    }

    @Override
    public Course getCourseById(int id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> {
                    String message = COURSE_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    public List<Course> getAllCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Course> slice = courseRepository.findAllCourses(pageable);
        if (slice.hasContent()) return slice.getContent();
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public void assignCourseToPrisoner(int courseId, int prisonerId) {
        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(getCourseById(courseId));
        enrollment.setPrisoner(prisonerRepository.findById(prisonerId)
                .orElseThrow(() -> {
                    String message = PRISONER_NOT_FOUND.formatted(prisonerId);
                    log.error(message);
                    return new NotFoundException(message);
                }));
        enrollment.setId(new Enrollment.EnrollmentId(courseId, prisonerId));
        enrollment.setStatus(Status.IN_PROGRESS);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public void deleteCourseFromPrisoner(int courseId, int prisonerId) {
        enrollmentRepository.deleteById(new Enrollment.EnrollmentId(courseId, prisonerId));
    }

    @Override
    @Transactional
    public void updateCourseOfPrisoner(Enrollment enrollment) {
        Enrollment old = findEnrollmentById(enrollment.getId().getCourseId(),
                enrollment.getId().getPrisonerId());

        if (old.getStatus() != null)
            old.setStatus(enrollment.getStatus());

        if (old.getGrade() != null)
            old.setGrade(enrollment.getGrade());

        if (old.getDate() != null)
            old.setDate(enrollment.getDate());

        enrollmentRepository.save(old);
    }

    @Override
    public Course findCourse(int prisonerId, int courseId) {
        return findEnrollmentById(courseId, prisonerId).getCourse();
    }

    private Enrollment findEnrollmentById(int courseId, int prisonerId) {
        return enrollmentRepository.findById(new Enrollment.EnrollmentId(courseId, prisonerId))
                .orElseThrow(() -> {
                    String message = ENROLLMENT_NOT_FOUND.formatted(courseId, prisonerId);
                    log.error(message);
                    return new NotFoundException(message);
                });
    }
}
