package org.prison.repositories;

import org.prison.model.prisoners.Prisoner;
import org.prison.model.staffs.Department;
import org.prison.model.staffs.Staff;
import org.prison.model.utils.DangerLevel;
import org.prison.model.utils.MarriageStatus;
import org.prison.model.utils.PrisonerStatus;
import org.prison.model.utils.Stat;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
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

    @Query(
            value = "SELECT * FROM prisoner AS p " +
                    "WHERE p.status = 'IMPRISONED'",
            nativeQuery = true
    )
    Slice<Prisoner> findAllImprisonedPrisoners(Pageable pageable);

    Slice<Prisoner> findAllByStatus(PrisonerStatus status, Pageable pageable);

    @Query(
            value = "SELECT TIMESTAMPDIFF(YEAR,p.dob,CURDATE()) AS age, COUNT(p.dob) AS count " +
                    "FROM prisoner AS p " +
                    "GROUP BY age",
            nativeQuery = true
    )
    List<Stat<Integer, Integer>> statisticsByAge();

    @Query(
            value = "SELECT p.crime_type, COUNT(p.crime_type) AS count " +
                    "FROM prisoner AS p " +
                    "GROUP BY p.crime_type",
            nativeQuery = true
    )
    List<Stat<String, Integer>> statisticsByCrimeType();

    @Query(
            value = "SELECT p.marriage, COUNT(p.marriage) AS count " +
                    "FROM prisoner AS p " +
                    "GROUP BY p.marriage",
            nativeQuery = true
    )
    List<Stat<MarriageStatus, Integer>> statisticsByMarriage();

    @Query(
            value = "SELECT p.ed_level, COUNT(p.ed_level) AS count " +
                    "FROM prisoner AS p " +
                    "GROUP BY p.ed_level",
            nativeQuery = true
    )
    List<Stat<String, Integer>> statisticsByEdLevel();

    @Query(
            value = "SELECT d.name, COUNT(p.dept_id) " +
                    "FROM prisoner AS p INNER JOIN prison.department AS d ON p.dept_id = d.id " +
                    "GROUP BY d.name",
            nativeQuery = true
    )
    List<Stat<String, Integer>> statisticsByDistrict();

    @Query(
            value = "SELECT p.d_level, COUNT(p.d_level) AS count " +
                    "FROM prisoner AS p " +
                    "GROUP BY p.d_level",
            nativeQuery = true
    )
    List<Stat<DangerLevel, Integer>> statisticsByDLevel();

    Slice<Prisoner> findAllByDept(Department dept, Pageable pageable);

    Slice<Prisoner> findAllByRespStaff(Staff staff, Pageable pageable);

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

    Slice<Prisoner> findAllByDlevelContaining(String dLevel, Pageable pageable);

}
