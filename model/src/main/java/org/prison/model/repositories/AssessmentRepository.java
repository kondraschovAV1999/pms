package org.prison.model.repositories;


import org.prison.model.data.staffs.Assessment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {
    @Query(
            value = "SELECT * FROM assessment " +
                    "WHERE staff_id = ?1",
            nativeQuery = true
    )
    Slice<Assessment> findAllByStaffId(int staffId, Pageable pageable);
}
