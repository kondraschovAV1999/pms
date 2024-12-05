package org.prison.model.repositories;


import org.prison.model.data.edu.Degree;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DegreeRepository extends JpaRepository<Degree, Integer> {
    @Query(value = "SELECT * FROM degree",
            nativeQuery = true)
    Slice<Degree> findAllDegrees(Pageable pageable);
}
