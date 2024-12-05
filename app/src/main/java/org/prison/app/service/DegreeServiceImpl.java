package org.prison.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prison.app.exceptions.NotFoundException;
import org.prison.model.data.edu.Course;
import org.prison.model.data.edu.Degree;
import org.prison.model.data.edu.PrisonerDegree;
import org.prison.model.data.utils.Status;
import org.prison.model.repositories.CourseRepository;
import org.prison.model.repositories.DegreeRepository;
import org.prison.model.repositories.PrisonerDegreeRepository;
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
public class DegreeServiceImpl implements DegreeService {

    private final DegreeRepository degreeRepository;
    private final PrisonerDegreeRepository prisonerDegreeRepository;
    private final PrisonerRepository prisonerRepository;

    private static final String DEGREE_NOT_FOUND = "Degree with id=%d Not Found";
    public static final String PRISONER_DEGREE_NOT_FOUND =
            "PrisonerDegree with degreeId=%d and prisonerId=%d Not Found";
    private static final String PRISONER_NOT_FOUND = "Prisoner with id=%d Not Found";
    private static final String COURSE_NOT_FOUND = "Course with id=%d Not Found";
    private final CourseRepository courseRepository;

    @Override
    public Degree createDegree(Degree degree) {
        return degreeRepository.save(degree);
    }

    @Override
    public void deleteDegree(int id) {
        degreeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateDegree(int id, Degree degree) {
        Degree oldDegree = getDegreeById(id);

        if (degree.getName() != null)
            oldDegree.setName(degree.getName());

        if (degree.getField() != null)
            oldDegree.setField(degree.getField());

        if (degree.getLevel() != null)
            oldDegree.setLevel(degree.getLevel());

        if (degree.getCourses() != null && !degree.getCourses().isEmpty()) {
            oldDegree.getCourses().clear();
            oldDegree.getCourses().addAll(degree.getCourses());
        }

        degreeRepository.save(oldDegree);
    }

    @Override
    public Degree getDegreeById(int id) {
        return degreeRepository.findById(id)
                .orElseThrow(() -> {
                    String message = DEGREE_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    public List<Degree> getAllDegrees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Degree> slice = degreeRepository.findAllDegrees(pageable);
        if (slice.hasContent()) return slice.getContent();
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public void assignDegreeToPrisoner(int degreeId, int prisonerId) {
        PrisonerDegree prisonerDegree = new PrisonerDegree();
        prisonerDegree.setDegree(getDegreeById(degreeId));
        prisonerDegree.setPrisoner(prisonerRepository.findById(prisonerId)
                .orElseThrow(() -> {
                    String message = PRISONER_NOT_FOUND.formatted(prisonerId);
                    log.error(message);
                    return new NotFoundException(message);
                }));

        prisonerDegree.setId(new PrisonerDegree.DegreeEnrollmentId(degreeId, prisonerId));
        prisonerDegree.setStatus(Status.IN_PROGRESS);
        prisonerDegreeRepository.save(prisonerDegree);
    }

    @Override
    public void deleteDegreeFromPrisoner(int degreeId, int prisonerId) {
        prisonerDegreeRepository.deleteById(
                new PrisonerDegree.DegreeEnrollmentId(degreeId, prisonerId));
    }

    @Override
    @Transactional
    public void updateDegreeOfPrisoner(PrisonerDegree prisonerDegree) {
        PrisonerDegree old = findPrisonerDegreeById(
                prisonerDegree.getId().getDegreeId(),
                prisonerDegree.getId().getPrisonerId());

        if (prisonerDegree.getStatus() != null)
            old.setStatus(prisonerDegree.getStatus());

        if (prisonerDegree.getDate() != null)
            old.setDate(prisonerDegree.getDate());

        prisonerDegreeRepository.save(old);
    }

    @Override
    public Degree findDegree(int prisonerId, int degreeId) {
        return findPrisonerDegreeById(degreeId, prisonerId).getDegree();
    }

    @Override
    @Transactional
    public void associateCourseToDegree(int courseId, int degreeId) {
        Degree degree = getDegreeById(degreeId);
        Course course = courseRepository.findById(degreeId).orElseThrow(() -> {
            String message = COURSE_NOT_FOUND.formatted(courseId);
            log.error(message);
            return new NotFoundException(message);
        });
        degree.getCourses().add(course);
        degreeRepository.save(degree);
    }

    @Override
    @Transactional
    public void removerCourseFromDegree(int courseId, int degreeId) {
        Degree degree = getDegreeById(degreeId);
        Course course = courseRepository.findById(degreeId).orElseThrow(() -> {
            String message = COURSE_NOT_FOUND.formatted(courseId);
            log.error(message);
            return new NotFoundException(message);
        });
        degree.getCourses().remove(course);
        degreeRepository.save(degree);
    }

    @Override
    public List<Course> getAllCourses(int id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Course> slice = courseRepository.findCoursesByDegreeId(id, pageable);
        if (slice.hasContent()) return slice.getContent();
        return new ArrayList<>();
    }

    private PrisonerDegree findPrisonerDegreeById(int degreeId, int prisonerId) {
        return prisonerDegreeRepository.findById(
                        new PrisonerDegree.DegreeEnrollmentId(degreeId, prisonerId))
                .orElseThrow(() -> {
                    String message = PRISONER_DEGREE_NOT_FOUND.formatted(degreeId, prisonerId);
                    log.error(message);
                    return new NotFoundException(message);
                });
    }
}
