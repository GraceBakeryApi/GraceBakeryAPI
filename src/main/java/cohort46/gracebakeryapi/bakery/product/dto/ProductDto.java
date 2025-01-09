package cohort46.gracebakeryapi.bakery.product.dto;

import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.filter.dto.FilterDto;
import cohort46.gracebakeryapi.bakery.ingredient.dto.IngredientDto;
import cohort46.gracebakeryapi.bakery.image.dto.ImageDto;
import cohort46.gracebakeryapi.helperclasses.SizePrice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {
    @NotNull
    @Setter
    private Long id;

    private String title_de;
    private String title_ru;
    private String description_de;
    private String description_ru;
    private Boolean isActive;

    @NotNull
    //@Setter
    private Long categoryid;

    @JsonProperty("image")
    private Set<ImageDto> images;


    private Set<IngredientDto> ingredients;


    private Set<BakeryoptionalDto> bakeryoptionals;

    private Set<FilterDto> filters;

    @JsonProperty("sizeprices")
    private Set<SizePrice> sizeprices = new HashSet<>();


}
//{product_id*, category_id, title_de, title_ru, description_de, description_ru , [image,...], [option_id,...], [ingridient_id,...], [<size, price>,....], is_active}
