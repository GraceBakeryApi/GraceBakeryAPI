package cohort46.gracebakeryapi.bakery.category.model;

import cohort46.gracebakeryapi.bakery.section.model.Section;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    //@Setter(AccessLevel.NONE)
    private Long id;
    private String title_de;
    private String title_ru;
    private String description_de;
    private String description_ru;
    private String image;
    @ManyToOne
    private Section section;
    @Column(nullable = false)
    private Boolean isActive;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();
}
// {category_id* , section_id, title, description, image, is_active}
