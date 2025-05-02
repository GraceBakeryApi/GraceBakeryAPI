package cohort46.gracebakeryapi.order.closeddate.model;

import cohort46.gracebakeryapi.accounting.model.UserAccount;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class ClosedDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long date;

    private String comment;
}
