package org.prison.model.repositories;


import org.prison.model.data.edu.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Enrollment.EnrollmentId> {
}
