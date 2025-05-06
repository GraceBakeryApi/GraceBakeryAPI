package cohort46.gracebakeryapi.bakery.bakeryoptional.model;

import cohort46.gracebakeryapi.bakery.optionsize.model.Optionsize;
import cohort46.gracebakeryapi.order.orderitem.model.Orderitem;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import jakarta.persistence.*;
import lombok.*;
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
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bakeryoptional {
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
    private String image;

    @OneToMany(mappedBy = "bakeryoptional", cascade = CascadeType.ALL)
    private Set<Optionsize> optionsizes = new HashSet<>();

    @ManyToMany()
    private Set<Product> products = new HashSet<>();

    @ManyToMany
    private Set<Orderitem> orderitems = new HashSet<>();

    @Column(nullable = false)
    private Boolean isActive;

}

