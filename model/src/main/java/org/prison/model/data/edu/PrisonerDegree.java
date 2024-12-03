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
public class PrisonerDegree {

    @EmbeddedId
    private DegreeEnrollmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("prisonerId")
    @JsonBackReference
    private Prisoner prisoner;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("degreeId")
    @JsonBackReference
    private Degree degree;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Status status;

    public PrisonerDegree(Prisoner prisoner, Degree degree) {
        this.prisoner = prisoner;
        this.degree = degree;
        this.id = new DegreeEnrollmentId(prisoner.getId(), degree.getId());
    }

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
