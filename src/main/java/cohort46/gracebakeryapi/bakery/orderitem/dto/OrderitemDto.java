package cohort46.gracebakeryapi.bakery.orderitem.dto;

import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.filter.dto.FilterDto;
import cohort46.gracebakeryapi.bakery.image.dto.ImageDto;
import cohort46.gracebakeryapi.bakery.ingredient.dto.IngredientDto;
import cohort46.gracebakeryapi.bakery.order.model.Order;
import cohort46.gracebakeryapi.bakery.product.model.Product;
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
public class OrderitemDto {
    @NotNull
    @Setter
    private Long id;

    @NotNull
    private Long orderid;

    @NotNull
    private Long productid;

    private Set<BakeryoptionalDto> bakeryoptionals  = new HashSet<>();;

    private Long sizeid;

    private Long ingredientid;

    private Integer quantity;

    private Double cost;

    private String comment;
}
