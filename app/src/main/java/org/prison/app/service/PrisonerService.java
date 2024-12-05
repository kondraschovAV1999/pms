package org.prison.app.service;


import org.prison.model.data.edu.Course;
import org.prison.model.data.edu.Degree;
import org.prison.model.data.prisoners.Communication;
import org.prison.model.data.prisoners.Prisoner;
import org.prison.model.data.utils.StatisticsReq;
import org.prison.model.data.utils.StatisticsResp;

import java.util.List;

public interface PrisonerService {
    Prisoner findById(int id);

    Prisoner savePrisoner(int deptId, Prisoner prisoner);

    Prisoner savePrisoner(Prisoner prisoner);

    void deletePrisonerById(int id);

    List<Prisoner> findAllPrisoners(int page, int size);

    StatisticsResp getStatistics(StatisticsReq statReq);

    List<Prisoner> findPrisonersByKeyword(String keyword, String filter, int page, int size);

    Prisoner editPrisoner(int id, int deptId, Prisoner prisoner);

    List<Course> findAllCourses(int id);

    List<Degree> findAllDegrees(int id);


    List<Prisoner> findReleasePrisoners(int page, int size);

    List<Prisoner> findAllByStatus(String status, int page, int size);

    List<Prisoner> findAllByDept(int deptId, int page, int size);

    List<Prisoner> findAllByRespStaff(int staffId, int page, int size);

    List<Communication> findAllCommunications(int id);

    Communication findCommunicationById(int commId);

    Communication addCommunication(int id, Communication communication);

    Prisoner deleteCommunication(int prisonerId, int commId);

    int numberPrisoners();
}
