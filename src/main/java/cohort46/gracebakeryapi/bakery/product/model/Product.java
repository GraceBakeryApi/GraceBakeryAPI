package cohort46.gracebakeryapi.bakery.product.model;

import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.category.model.Category;
import cohort46.gracebakeryapi.bakery.filter.model.Filter;
import cohort46.gracebakeryapi.bakery.image.model.Image;
import cohort46.gracebakeryapi.bakery.ingredient.model.Ingredient;
import cohort46.gracebakeryapi.bakery.orderitem.model.Orderitem;
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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    //@Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne//( cascade = CascadeType.ALL)
    private Category category;

    private String title_de;
    private String title_ru;
    private String description_de;
    private String description_ru;

    private String topimage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Image> images = new HashSet<>();

    @ManyToMany
    private Set<Ingredient> ingredients = new HashSet<>();

    @ManyToMany
    private Set<Bakeryoptional> bakeryoptionals = new HashSet<>();

    @ManyToMany
    private Set<Filter> filters = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Productsize> productsizes = new HashSet<>();

    @Column(nullable = false)
    private Boolean isActive;
//-------------------------------------------------------------------
    @OneToMany(mappedBy = "product")
    private Set<Orderitem> orderitems = new HashSet<>();
}

/*
{product_id*, category_id, title_de, title_ru, description_de, description_ru , [image,...], [option_id,...], [ingridient_id,...], [<size, price>,....], is_active}

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductSize> productSizes = new HashSet<>();
    //private List<ProductSize> productSizes;

 */
