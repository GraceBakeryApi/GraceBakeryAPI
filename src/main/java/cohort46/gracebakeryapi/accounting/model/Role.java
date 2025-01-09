package cohort46.gracebakeryapi.accounting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "t_role")
public class Role {
    @Id
    private Long id;
    private String name;
    /*
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

     */
}