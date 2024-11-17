package org.prison.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "ENROLLMENT")
public class Enrollment {

    @EmbeddedId
    private EnrollmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("prisonerId")
    private Prisoner prisoner;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    private Course course;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Float grade;

    private LocalDate date;

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
        private Integer prisonerId;
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
