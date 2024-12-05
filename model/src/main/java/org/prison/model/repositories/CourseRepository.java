package org.prison.model.repositories;


import org.prison.model.data.edu.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query(value = "SELECT * FROM course",
            nativeQuery = true)
    Slice<Course> findAllCourses(Pageable pageable);

    @Query(value = "SELECT c.* FROM degree_course " +
            "INNER JOIN course c on degree_course.course_id = c.id " +
            "WHERE degree_id = ?1",
    nativeQuery = true)
    Slice<Course> findCoursesByDegreeId(int degreeId, Pageable pageable);
}
