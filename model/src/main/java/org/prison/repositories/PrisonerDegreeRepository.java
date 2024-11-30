package org.prison.repositories;

import org.prison.model.edu.PrisonerDegree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrisonerDegreeRepository extends JpaRepository<PrisonerDegree, PrisonerDegree.DegreeEnrollmentId> {
}
