package cohort46.gracebakeryapi.bakery.optionsize.model;

import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.section.model.Section;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Optionsize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    //@Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    private Size size;

    @NonNull
    @Column(nullable = false)
    private double price;

    @ManyToOne
    private Bakeryoptional bakeryoptional;
}



