package org.prison.model.data.staffs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private StaffLevel level;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "staff_duties",
            joinColumns = @JoinColumn(name = "staff_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "duty_id", referencedColumnName = "id"))
    private List<Duty> duties = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "staff_assignments",
            joinColumns = @JoinColumn(name = "staff_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "assignment_id", referencedColumnName = "id"))
    private List<Assignment> assignments = new ArrayList<>();


    @OneToMany
    @JoinColumn(name = "master_id")
    private List<Staff> supervisee = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name ="staff_id")
    private List<Assessment> assessments = new ArrayList<>();

    @OneToMany
    @JoinColumn(name ="staff_id")
    private List<Prisoner> prisoners = new ArrayList<>();

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
