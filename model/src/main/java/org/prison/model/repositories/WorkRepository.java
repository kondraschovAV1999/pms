package org.prison.model.repositories;


import org.prison.model.data.prisoners.Work;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkRepository extends JpaRepository<Work, Integer> {
    @Query(
            value = "SELECT * FROM work",
            nativeQuery = true
    )
    Slice<Work> findWorkList(Pageable pageable);
}
