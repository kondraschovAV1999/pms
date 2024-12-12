package org.prison.model.data.prisoners;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.prison.model.data.edu.Enrollment;
import org.prison.model.data.edu.PrisonerDegree;
import org.prison.model.data.utils.DangerLevel;
import org.prison.model.data.utils.MarriageStatus;
import org.prison.model.data.utils.PrisonerStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
//@Builder
@AllArgsConstructor
@Entity
public class Prisoner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String fname;

    @Column(nullable = false)
    private String lname;

    @Column(nullable = false)
    private LocalDate dob;
    @Column(nullable = false)
    private String idNumber;
    @Column(nullable = false)
    private String idType;
    private String address;

    @Enumerated(EnumType.STRING)
    private MarriageStatus marriage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DangerLevel dLevel;

    private String crimeType;

    @Enumerated(EnumType.STRING)
    private PrisonerStatus status;

    private String edLevel;

    @Column(nullable = false)
    private String term;

    @ManyToOne
    @JoinColumn(name = "work_id", referencedColumnName = "id")
    private Work work;

    @Column(nullable = false)
    private LocalDate relDate;

    @JsonManagedReference(value = "prisoner-degrees")
    @OneToMany(mappedBy = "prisoner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrisonerDegree> degrees = new ArrayList<>();

    @JsonManagedReference(value = "prisoner-courses")
    @OneToMany(mappedBy = "prisoner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> courses = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "prisoner_id")
    private List<Communication> communications = new ArrayList<>();

    /* Managing degrees bidirectional relationship */
    public void addDegree(PrisonerDegree prisonerDegree) {
        degrees.add(prisonerDegree);
        prisonerDegree.getDegree().getPrisoners().add(prisonerDegree);
    }

    public void removeDegree(PrisonerDegree prisonerDegree) {
        degrees.remove(prisonerDegree);
        prisonerDegree.getDegree().getPrisoners().remove(prisonerDegree);
    }

    /* Managing courses bidirectional relationship */
    public void addCourse(Enrollment enrollment) {
        courses.add(enrollment);
        enrollment.getCourse().getPrisoners().add(enrollment);
    }

    public void removeCourse(Enrollment enrollment) {
        courses.remove(enrollment);
        enrollment.getCourse().getPrisoners().remove(enrollment);
    }


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Prisoner other = (Prisoner) o;
        return getId() != null && Objects.equals(getId(), other.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}