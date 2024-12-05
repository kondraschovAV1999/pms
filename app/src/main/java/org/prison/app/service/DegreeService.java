package org.prison.app.service;

import org.prison.model.data.edu.Course;
import org.prison.model.data.edu.Degree;
import org.prison.model.data.edu.PrisonerDegree;

import java.util.List;

public interface DegreeService {
    Degree createDegree(Degree degree);
    void deleteDegree(int id);
    void updateDegree(int id, Degree degree);
    Degree getDegreeById(int id);
    List<Degree> getAllDegrees(int page, int size);
    void assignDegreeToPrisoner(int degreeId, int prisonerId);
    void deleteDegreeFromPrisoner(int degreeId, int prisonerId);
    void updateDegreeOfPrisoner(PrisonerDegree prisonerDegree);
    Degree findDegree(int prisonerId, int degreeId);
    void associateCourseToDegree(int courseId, int degreeId);
    void removerCourseFromDegree(int courseId, int degreeId);
    List<Course> getAllCourses(int id, int page, int size);
}
