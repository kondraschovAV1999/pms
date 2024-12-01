package org.prison.repositories;

import org.prison.model.prisoners.Work;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, Integer> {
    Slice<Work> findWorkList(Pageable pageable);
}
