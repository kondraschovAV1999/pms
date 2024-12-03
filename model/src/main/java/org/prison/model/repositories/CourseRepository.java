package org.prison.model.repositories;


import org.prison.model.data.edu.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
