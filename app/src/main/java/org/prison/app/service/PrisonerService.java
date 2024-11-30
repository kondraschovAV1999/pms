package org.prison.app.service;

import org.prison.model.edu.Course;
import org.prison.model.edu.Degree;
import org.prison.model.prisoners.Communication;
import org.prison.model.prisoners.Prisoner;
import org.prison.model.staffs.Department;
import org.prison.model.staffs.Staff;
import org.prison.model.utils.PrisonerStatus;
import org.prison.model.utils.StatisticsReq;
import org.prison.model.utils.StatisticsResp;

import java.util.List;

public interface PrisonerService {
    Prisoner findById(int id);
    Prisoner savePrisoner(Prisoner prisoner);
    void deletePrisonerById(int id);
    List<Prisoner> findAllPrisoners(int page, int size);
    StatisticsResp getStatistics(StatisticsReq statReq);
    List<Prisoner> findPrisonersByKeyword(String keyword, String filter, int page, int size);
    List<Course> findAllCourses(int id);
    List<Degree> findAllDegrees(int id);
    Course findCourseById(int prisonerId, int courseId);
    void addCourse(int prisonerId, Course course);
    void deleteCourse(int prisonerId, int courseId);
    Degree findDegreeById(int prisonerId, int degreeId);
    void addDegree(int prisonerId, Degree degree);
    void deleteDegree(int prisonerId, int degreeId);
    List<Prisoner> findReleasePrisoners(int page, int size);
    List<Prisoner> findAllImprisonedPrisoners(int page, int size);
    List<Prisoner> findAllByStatus(PrisonerStatus status, int page, int size);
    List<Prisoner> findAllByDept(Department dept, int page, int size);
    List<Prisoner> findAllByRespStaff(Staff staff, int page, int size);
    List<Communication> findAllCommunications(int id);
    Communication findCommunicationById(int commId);
    void addCommunication(int id, Communication communication);
    void deleteCommunication(int commId);

}
