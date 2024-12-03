package org.prison.model.repositories;


import org.prison.model.data.prisoners.Prisoner;
import org.prison.model.data.utils.DangerLevel;
import org.prison.model.data.utils.MarriageStatus;
import org.prison.model.data.utils.PrisonerStatus;
import org.prison.model.data.utils.Stat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrisonerRepository extends JpaRepository<Prisoner, Integer> {
    @Query(
            value = "SELECT count(*) FROM prisoner",
            nativeQuery = true
    )
    int countPrisoners();


    @Query(
            value = "SELECT * FROM prisoner AS p " +
                    "WHERE DATEDIFF(p.rel_date, CURRENT_DATE) < 7",
            nativeQuery = true
    )
    Slice<Prisoner> findReleasePrisoners(Pageable pageable);

    Slice<Prisoner> findAllByStatus(PrisonerStatus status, Pageable pageable);

    @Query(
            value = "SELECT TIMESTAMPDIFF(YEAR,p.dob,CURDATE()) AS age, COUNT(p.dob) AS count " +
                    "FROM prisoner AS p " +
                    "WHERE TIMESTAMPDIFF(YEAR, p.dob, CURDATE()) BETWEEN ?1 AND ?2 " +
                    "GROUP BY age",
            nativeQuery = true
    )
    List<Stat<Integer, Integer>> statisticsByAge(int ageStart, int ageEnd);

    @Query(
            value = "SELECT p.crime_type, COUNT(p.crime_type) AS count " +
                    "FROM prisoner AS p " +
                    "WHERE p.crime_type = ?1 " +
                    "GROUP BY p.crime_type",
            nativeQuery = true
    )
    List<Stat<String, Integer>> statisticsByCrimeType(String crimeType);

    @Query(
            value = "SELECT p.marriage, COUNT(p.marriage) AS count " +
                    "FROM prisoner AS p " +
                    "WHERE p.marriage = ?1 " +
                    "GROUP BY p.marriage",
            nativeQuery = true
    )
    List<Stat<MarriageStatus, Integer>> statisticsByMarriage(String marriageStatus);

    @Query(
            value = "SELECT p.ed_level, COUNT(p.ed_level) AS count " +
                    "FROM prisoner AS p " +
                    "WHERE p.ed_level = ?1 " +
                    "GROUP BY p.ed_level",
            nativeQuery = true
    )
    List<Stat<String, Integer>> statisticsByEdLevel(String edLevel);

    @Query(
            value = "SELECT d.name, COUNT(p.dept_id) " +
                    "FROM prisoner AS p INNER JOIN prison.department AS d ON p.dept_id = d.id " +
                    "WHERE d.name = ?1 " +
                    "GROUP BY d.name",
            nativeQuery = true
    )
    List<Stat<String, Integer>> statisticsByDistrict(String dName);

    @Query(
            value = "SELECT p.d_level, COUNT(p.d_level) AS count " +
                    "FROM prisoner AS p " +
                    "WHERE p.d_level = ?1 " +
                    "GROUP BY p.d_level",
            nativeQuery = true
    )
    List<Stat<DangerLevel, Integer>> statisticsByDLevel(String dangerLevel);

    Slice<Prisoner> findAllByDeptId(int deptId, Pageable pageable);

    Slice<Prisoner> findAllByRespStaffId(int staffId, Pageable pageable);

    @Query(
            value = "SELECT * FROM prisoner AS p WHERE " +
                    "CONCAT(p.fname,' ', p.lname) LIKE CONCAT('%', ?1, '%')",
            nativeQuery = true
    )
    Slice<Prisoner> findAllByName(String name, Pageable pageable);

    Slice<Prisoner> findAllByCrimeTypeContaining(String crimeType, Pageable pageable);

    Slice<Prisoner> findAllByIdTypeContaining(String idType, Pageable pageable);

    Slice<Prisoner> findAllByIdNumberContaining(String idNumber, Pageable pageable);

    Slice<Prisoner> findAllByTermContaining(String term, Pageable pageable);

    Slice<Prisoner> findAllByStatusContaining(PrisonerStatus status, Pageable pageable);

    Slice<Prisoner> findAllByDLevelContaining(String dLevel, Pageable pageable);

    @Query(
            value = "SELECT  * FROM prisoner",
            nativeQuery = true
    )
    Slice<Prisoner> findAllPrisoners(Pageable pageable);

}
