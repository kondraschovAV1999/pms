package org.prison.model.repositories;


import org.prison.model.data.staffs.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
