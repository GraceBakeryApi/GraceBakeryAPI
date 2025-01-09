package cohort46.gracebakeryapi.bakery.productsize.model;

import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Productsize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    //@Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Size size;

    @NonNull
    @Column(nullable = false)
    private double price;

    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;
}