package cohort46.gracebakeryapi.bakery.section.model;

import cohort46.gracebakeryapi.bakery.category.model.Category;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Section implements Serializable {
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
    @Column(nullable = false)
    private Boolean isActive;
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private Set<Category> categories = new HashSet<>();
}
/*
{section_id, title_de,  title_ru, description_de, description_ru, image, is_active}
*/