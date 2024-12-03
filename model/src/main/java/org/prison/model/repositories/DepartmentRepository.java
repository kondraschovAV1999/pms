package org.prison.model.repositories;


import org.prison.model.data.staffs.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
