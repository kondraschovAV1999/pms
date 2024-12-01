package org.prison.repositories;

import org.prison.model.staffs.Staff;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
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
}
