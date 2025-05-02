package cohort46.gracebakeryapi.bakery.size.model;

import cohort46.gracebakeryapi.bakery.optionsize.model.Optionsize;
import cohort46.gracebakeryapi.order.orderitem.model.Orderitem;
import cohort46.gracebakeryapi.bakery.productsize.model.Productsize;
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
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    //@Setter(AccessLevel.NONE)
    private Long id;
    private String title_de;
    private String title_ru;
    private double mass;
    private int diameter;
    private int persons;

    @OneToMany(mappedBy = "size", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Optionsize> optionsizes = new HashSet<>();

    @OneToMany(mappedBy = "size", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Productsize> productsizes = new HashSet<>();

    @OneToMany(mappedBy = "size")
    private Set<Orderitem> orderitems = new HashSet<>();

    @Column(nullable = false)
    private Boolean isActive;
}
// {size_id* , title_de, title_ru, mass, diametr, persons}
