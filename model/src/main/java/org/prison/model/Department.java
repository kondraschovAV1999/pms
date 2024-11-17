package org.prison.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "DEPARTMENT")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String Name;

    @ManyToMany
    @JoinTable(name = "ASSIGNMENT_PERMISSIONS",
            joinColumns = @JoinColumn(name = "DeptId", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PermissionId", referencedColumnName = "ID"))
    private List<Permission> permissions = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    private List<Duty> dutyList = new ArrayList<>();

    @OneToMany(mappedBy = "dept")
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "dept")
    private List<Prisoner> prisoners = new ArrayList<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Work> workList = new ArrayList<>();

    @OneToMany(mappedBy = "dept")
    private List<Staff> staff = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Department other = (Department) o;
        return getId() != null && Objects.equals(getId(), other.getId());
    }
    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
