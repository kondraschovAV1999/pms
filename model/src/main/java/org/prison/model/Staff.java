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
@Table(name = "STAFF")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer staffId;

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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private StaffLevel level;

    @ManyToOne
    @JoinColumn(name = "DeptId", referencedColumnName = "ID")
    private Department dept;

    @ManyToMany
    @JoinTable(name = "STAFF_DUTIES",
            joinColumns = @JoinColumn(name = "StaffId", referencedColumnName = "StaffId"),
            inverseJoinColumns = @JoinColumn(name = "DutyId", referencedColumnName = "ID"))
    private List<Duty> duties = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "STAFF_ASSIGNMENTS",
            joinColumns = @JoinColumn(name = "StaffId", referencedColumnName = "StaffId"),
            inverseJoinColumns = @JoinColumn(name = "AssignmentId", referencedColumnName = "ID"))
    private List<Assignment> assignments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="MasterId", referencedColumnName="StaffId")
    private Staff master;

    @OneToMany(mappedBy = "master")
    private List<Staff> supervisee =  new ArrayList<>();

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assessment> assessments =  new ArrayList<>();

    @OneToMany(mappedBy = "respStaff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prisoner> prisoners =  new ArrayList<>();

    public enum Gender {
        M, F
    }

    public enum StaffLevel {
        NORMAL_STAFF, MASTER, VICE_MASTER
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Staff other = (Staff) o;
        return getStaffId() != null && Objects.equals(getStaffId(), other.getStaffId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
