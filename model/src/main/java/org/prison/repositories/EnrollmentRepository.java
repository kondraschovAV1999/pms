package org.prison.repositories;

import org.prison.model.edu.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Enrollment.EnrollmentId> {
}
