package org.prison.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "PRISONER")
public class Prisoner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prisonerId;

    @Column(name = "Fname", nullable = false)
    private String fName;

    @Column(name = "Lname", nullable = false)
    private String lName;

    @Column(nullable = false)
    private LocalDate dob;
    @Column(nullable = false)
    private String idNumber;
    @Column(nullable = false)
    private String idType;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "Marriage")
    private MarriageStatus marriage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DangerLevel dLevel;

    private String crimeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private PrisonerStatus status;

    private String edLevel;

    @Column(nullable = false)
    private String term;

    @ManyToOne
    @JoinColumn(name = "StaffId", referencedColumnName = "StaffId")
    private Staff respStaff;

    @ManyToOne
    @JoinColumn(name = "WorkId", referencedColumnName = "ID")
    private Work work;

    @ManyToOne
    @JoinColumn(name = "DeptId", referencedColumnName = "ID")
    private Department dept;

    @Column(nullable = false)
    private LocalDate relDate;

    @OneToMany(mappedBy = "prisoner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrisonerDegree> degrees = new ArrayList<>();

    @OneToMany(mappedBy = "prisoner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> courses = new ArrayList<>();

    @OneToMany(mappedBy = "prisoner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Communication> communications = new ArrayList<>();

    public enum MarriageStatus {
        SINGLE, DIVORCED, MARRIED, WIDOWED
    }

    public enum DangerLevel {
        A, B, C, D, E
    }

    public enum PrisonerStatus {
        IMPRISONED, RELEASED
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Prisoner other = (Prisoner) o;
        return getPrisonerId() != null && Objects.equals(getPrisonerId(), other.getPrisonerId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
