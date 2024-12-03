package org.prison.model.data.edu;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.prison.model.data.prisoners.Prisoner;
import org.prison.model.data.utils.Status;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
//@Builder
@AllArgsConstructor
@Entity
public class Enrollment {

    @EmbeddedId
    private EnrollmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("prisonerId")
    @JsonBackReference
    private Prisoner prisoner;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    @JsonBackReference
    private Course course;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Float grade;

    private LocalDate date;

    public Enrollment(Prisoner prisoner, Course course) {
        this.prisoner = prisoner;
        this.course = course;
        this.id = new EnrollmentId(prisoner.getId(), course.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Enrollment that = (Enrollment) o;
        return Objects.equals(prisoner, that.prisoner) &&
                Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prisoner, course);
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @Embeddable
    public static class EnrollmentId implements Serializable {
        @Column(name = "prisoner_id")
        private Integer prisonerId;

        @Column(name = "course_id")
        private Integer courseId;

        public EnrollmentId(Integer prisonerId, Integer courseId) {
            this.prisonerId = prisonerId;
            this.courseId = courseId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass())
                return false;

            EnrollmentId that = (EnrollmentId) o;
            return Objects.equals(prisonerId, that.prisonerId) &&
                    Objects.equals(courseId, that.courseId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(prisonerId, courseId);
        }
    }

}
