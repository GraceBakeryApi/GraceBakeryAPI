package cohort46.gracebakeryapi.bakery.section.dto;

import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Set;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionDto {
    @NotNull
    @Setter
    private Long id ;
    private String title_de;
    private String title_ru;
    private String description_de;
    private String description_ru;
    @Setter
    private String image;
    private Boolean isActive;

    @JsonProperty("category")
    private Set<CategoryDto> categories;
}

/*
// {section_id, title_de,  title_ru, description_de, description_ru, image, is_active}


 */

