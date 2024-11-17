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
@Table(name = "ASSIGNMENT")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "DeptId", referencedColumnName = "ID")
    private Department dept;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ASSIGNMENT_PERMISSIONS",
            joinColumns = @JoinColumn(name = "AssignmentId", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PermissionId", referencedColumnName = "ID"))
    private List<Permission> permissions = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Assignment other = (Assignment) o;
        return getId() != null && Objects.equals(getId(), other.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
