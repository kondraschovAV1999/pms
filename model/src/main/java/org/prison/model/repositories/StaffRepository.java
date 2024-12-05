package org.prison.model.repositories;


import org.prison.model.data.prisoners.Prisoner;
import org.prison.model.data.staffs.Staff;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE staff SET dept_id = :deptId WHERE id = :staffId",
            nativeQuery = true)
    void assignStaffToDepartment(@Param("deptId") int deptId,
                                    @Param("staffId") int staffId);

    @Query(
            value = "SELECT * FROM staff",
            nativeQuery = true
    )
    Slice<Staff> findAllStaffs(Pageable pageable);

    @Query(
            value = "SELECT master.* FROM prison.staff staff " +
                    "INNER JOIN prison.staff AS master ON staff.master_id = master.id " +
                    "WHERE staff.id = ?1",
            nativeQuery = true
    )
    Optional<Staff> findSupervisor(int id);

    @Query(
            value = "SELECT staff.* FROM prison.staff master " +
                    "INNER JOIN prison.staff AS staff ON staff.master_id = master.id " +
                    "WHERE master.id = ?1",
            nativeQuery = true
    )
    Slice<Staff> findAllSupervisee(int id, Pageable pageable);

    @Query(
            value = "SELECT * FROM prisoner " +
                    "WHERE staff_id = ?1",
            nativeQuery = true
    )
    Slice<Prisoner> findAllPrisoners(int id, Pageable pageable);
}
