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
@Table(name = "PRISONER_DEGREE")
public class PrisonerDegree {

    @EmbeddedId
    private DegreeEnrollmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("prisonerId")
    private Prisoner prisoner;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("degreeId")
    private Degree degree;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        PrisonerDegree that = (PrisonerDegree) o;
        return Objects.equals(prisoner, that.prisoner) &&
                Objects.equals(degree, that.degree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prisoner, degree);
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @Embeddable
    public static class DegreeEnrollmentId implements Serializable {
        private Integer prisonerId;
        private Integer degreeId;

        public DegreeEnrollmentId(Integer prisonerId, Integer degreeId) {
            this.prisonerId = prisonerId;
            this.degreeId = degreeId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass())
                return false;

            DegreeEnrollmentId that = (DegreeEnrollmentId) o;
            return Objects.equals(prisonerId, that.prisonerId) &&
                    Objects.equals(degreeId, that.degreeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(prisonerId, degreeId);
        }
    }

}
