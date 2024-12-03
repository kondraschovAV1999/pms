package org.prison.model.repositories;


import org.prison.model.data.edu.PrisonerDegree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrisonerDegreeRepository extends JpaRepository<PrisonerDegree, PrisonerDegree.DegreeEnrollmentId> {
}
