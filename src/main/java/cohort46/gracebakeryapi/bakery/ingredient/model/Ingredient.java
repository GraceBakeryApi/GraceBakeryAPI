package cohort46.gracebakeryapi.bakery.ingredient.model;

import cohort46.gracebakeryapi.bakery.orderitem.model.Orderitem;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
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

    @ManyToMany(cascade = CascadeType.ALL)//////////////////
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private Set<Orderitem> orderitems = new HashSet<>();
}