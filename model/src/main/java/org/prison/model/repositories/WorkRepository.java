package org.prison.model.repositories;


import org.prison.model.data.prisoners.Work;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface WorkRepository extends JpaRepository<Work, Integer> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE work SET dept_id = :deptId WHERE id = :workId",
            nativeQuery = true)
    void assignWorkToDepartment(@Param("deptId") int deptId,
                                @Param("workId") int workId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE work SET dept_id = NULL WHERE id = :workId AND dept_id= :deptId",
            nativeQuery = true)
    void removeWorkFromDepartment(@Param("deptId") int deptId,
                                @Param("workId") int workId);


    @Query(
            value = "SELECT * FROM work",
            nativeQuery = true
    )
    Slice<Work> findWorkList(Pageable pageable);
}
