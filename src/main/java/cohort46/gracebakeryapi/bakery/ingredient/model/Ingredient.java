package cohort46.gracebakeryapi.bakery.ingredient.model;

import cohort46.gracebakeryapi.order.orderitem.model.Orderitem;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    //@Setter(AccessLevel.NONE)
    private Long id;
    private String title_de;
    private String title_ru;
    private String description_de;
    private String description_ru;

    @Column(nullable = false)
    private String image_de;

    @Column(nullable = false)
    private String image_ru;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToMany
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "ingredient")
    private Set<Orderitem> orderitems = new HashSet<>();
}