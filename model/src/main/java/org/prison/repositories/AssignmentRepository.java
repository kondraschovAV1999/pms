package org.prison.repositories;

import org.prison.model.staffs.Assignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Query(
            value = "SELECT assignment.* FROM assignment " +
                    "INNER JOIN staff_assignments sa on assignment.id = sa.assignment_id " +
                    "WHERE sa.staff_id = ?1",
            nativeQuery = true
    )
    Slice<Assignment> findAllByStaffId(int staffId, Pageable pageable);
}
