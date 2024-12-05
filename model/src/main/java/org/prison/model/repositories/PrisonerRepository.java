package org.prison.model.repositories;


import org.prison.model.data.prisoners.Prisoner;
import org.prison.model.data.utils.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PrisonerRepository extends JpaRepository<Prisoner, Integer> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE prisoner SET dept_id = :deptId WHERE id = :prisonerId",
            nativeQuery = true)
    void assignPrisonerToDepartment(@Param("deptId") int deptId,
                                    @Param("prisonerId") int prisonerId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE prisoner SET staff_id = :staffId WHERE id = :prisonerId",
            nativeQuery = true)
    void assignPrisonerToStaff(@Param("staffId") int staffId,
                               @Param("prisonerId") int prisonerId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE prisoner SET staff_id = NULL WHERE id = :prisonerId AND staff_id = :staffId",
            nativeQuery = true)
    void removePrisonerFromStaff(@Param("staffId") int staffId,
                               @Param("prisonerId") int prisonerId);

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


    @Query(
            value = "SELECT TIMESTAMPDIFF(YEAR,p.dob,CURDATE()) AS age, COUNT(p.dob) AS count " +
                    "FROM prisoner AS p " +
                    "WHERE TIMESTAMPDIFF(YEAR, p.dob, CURDATE()) BETWEEN ?1 AND ?2 " +
                    "GROUP BY age",
            nativeQuery = true
    )
    List<IntegerProjection> statisticsByAge(int ageStart, int ageEnd);

    @Query(
            value = "SELECT p.crime_type AS string, COUNT(p.crime_type) AS count " +
                    "FROM prisoner AS p " +
                    "WHERE p.crime_type = ?1 " +
                    "GROUP BY p.crime_type",
            nativeQuery = true
    )
    List<StringProjection> statisticsByCrimeType(String crimeType);

    @Query(
            value = "SELECT p.marriage AS string, COUNT(p.marriage) AS count " +
                    "FROM prisoner AS p " +
                    "WHERE p.marriage = ?1 " +
                    "GROUP BY p.marriage",
            nativeQuery = true
    )
    List<StringProjection> statisticsByMarriage(String marriageStatus);

    @Query(
            value = "SELECT p.ed_level AS string, COUNT(p.ed_level) AS count " +
                    "FROM prisoner AS p " +
                    "WHERE p.ed_level = ?1 " +
                    "GROUP BY p.ed_level",
            nativeQuery = true
    )
    List<StringProjection> statisticsByEdLevel(String edLevel);

    @Query(
            value = "SELECT d.name AS string, COUNT(p.dept_id) " +
                    "FROM prisoner AS p INNER JOIN prison.department AS d ON p.dept_id = d.id " +
                    "WHERE d.name = ?1 " +
                    "GROUP BY d.name",
            nativeQuery = true
    )
    List<StringProjection> statisticsByDistrict(String dName);

    @Query(
            value = "SELECT p.d_level AS string, COUNT(p.d_level) AS count " +
                    "FROM prisoner AS p " +
                    "WHERE p.d_level = ?1 " +
                    "GROUP BY p.d_level",
            nativeQuery = true
    )
    List<StringProjection> statisticsByDLevel(String dangerLevel);

    @Query(
            value = "SELECT * FROM prisoner " +
                    "WHERE dept_id = ?1"
            , nativeQuery = true)
    Slice<Prisoner> findAllByDeptId(int deptId, Pageable pageable);

    @Query(
            value = "SELECT * FROM prisoner " +
                    "WHERE staff_id = ?1"
            , nativeQuery = true)
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

    Slice<Prisoner> findAllByStatus(PrisonerStatus status, Pageable pageable);

    Slice<Prisoner> findAllByDLevel(DangerLevel dLevel, Pageable pageable);

    @Query(
            value = "SELECT  * FROM prisoner",
            nativeQuery = true
    )
    Slice<Prisoner> findAllPrisoners(Pageable pageable);

}
