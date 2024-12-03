package org.prison.model.data.staffs;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.prison.model.data.prisoners.Prisoner;
import org.prison.model.data.utils.Gender;
import org.prison.model.data.utils.StaffLevel;

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
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column( nullable = false)
    private String fname;

    @Column( nullable = false)
    private String lname;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private String idNumber;

    @Column(nullable = false)
    private String idType;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private StaffLevel level;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", referencedColumnName = "id")
    private Department dept;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "staff_duties",
            joinColumns = @JoinColumn(name = "staff_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "duty_id", referencedColumnName = "id"))
    private List<Duty> duties = new ArrayList<>();

    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "staff_assignments",
            joinColumns = @JoinColumn(name = "staff_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "assignment_id", referencedColumnName = "id"))
    private List<Assignment> assignments = new ArrayList<>();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="master_id", referencedColumnName="id")
    private Staff master;

    @JsonManagedReference
    @OneToMany(mappedBy = "master")
    private List<Staff> supervisee =  new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assessment> assessments =  new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "respStaff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prisoner> prisoners =  new ArrayList<>();

    /* Managing duties bidirectional relationship */
    public void addDuty(Duty duty) {
        if (!duties.contains(duty)) {
            duties.add(duty);
            duty.getStaffs().add(this);
        }
    }

    public void removeDuty(Duty duty) {
        if (duties.contains(duty)) {
            duties.remove(duty);
            duty.getStaffs().remove(this);
        }
    }

    /* Managing supervisee bidirectional relationship */
    public void addSupervisee(Staff superviseeStaff) {
        if (!supervisee.contains(superviseeStaff)) {
            supervisee.add(superviseeStaff);
            superviseeStaff.setMaster(this);
        }
    }

    public void removeSupervisee(Staff superviseeStaff) {
        if (supervisee.contains(superviseeStaff)) {
            supervisee.remove(superviseeStaff);
            superviseeStaff.setMaster(null);
        }
    }

    /* Managing assessments bidirectional relationship */
    public void addAssessment(Assessment assessment) {
        if (!assessments.contains(assessment)) {
            assessments.add(assessment);
            assessment.setStaff(this);
        }
    }

    public void removeAssessment(Assessment assessment) {
        if (assessments.contains(assessment)) {
            assessments.remove(assessment);
            assessment.setStaff(null);
        }
    }

    /* Managing prisoners bidirectional relationship */
    public void addPrisoner(Prisoner prisoner) {
        if (!prisoners.contains(prisoner)) {
            prisoners.add(prisoner);
            prisoner.setRespStaff(this);
        }
    }

    public void removePrisoner(Prisoner prisoner) {
        if (prisoners.contains(prisoner)) {
            prisoners.remove(prisoner);
            prisoner.setRespStaff(null);
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Staff other = (Staff) o;
        return getId() != null && Objects.equals(getId(), other.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
