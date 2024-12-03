package org.prison.model.repositories;


import org.prison.model.data.edu.Degree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DegreeRepository extends JpaRepository<Degree, Integer> {
}
