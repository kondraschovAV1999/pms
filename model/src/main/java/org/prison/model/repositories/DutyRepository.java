package org.prison.model.repositories;


import org.prison.model.data.staffs.Duty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface DutyRepository extends JpaRepository<Duty, Integer> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE duty SET dept_id = :deptId WHERE id = :dutyId",
            nativeQuery = true)
    void assignDutyToDepartment(@Param("deptId") int deptId,
                                @Param("dutyId") int dutyId);


    Slice<Duty> findAllByDate(LocalDate date, Pageable pageable);

    @Query(
            value = "SELECT d.* FROM staff_duties AS sd " +
                    "JOIN duty AS d ON sd.duty_id = d.id " +
                    "WHERE  sd.staff_id = ?1",
            nativeQuery = true
    )
    Slice<Duty> findAllByStaffId(int staffId, Pageable pageable);

    List<Duty> findAllByDate(LocalDate date);
}
