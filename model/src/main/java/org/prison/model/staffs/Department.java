package org.prison.model.staffs;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.prison.model.prisoners.Prisoner;
import org.prison.model.prisoners.Work;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String Name;

    @ManyToMany
    @JoinTable(name = "dept_permissions",
            joinColumns = @JoinColumn(name = "dept_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private List<Permission> permissions = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "department")
    private List<Duty> dutyList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "dept")
    private List<Assignment> assignments = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "dept")
    private List<Prisoner> prisoners = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Work> workList = new ArrayList<>();

    @JsonManagedReference
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
