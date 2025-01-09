package cohort46.gracebakeryapi.bakery.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;


@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class findProductsByPriceDto {
    @NotNull
    private Long id;
    private String title_de;
    private String title_ru;
    private String description_de;
    private String description_ru;
    private Boolean isActive;
    private Long categoryid;
    private Integer persons;
    private Double price;
    private String image;


}
//{product_id*, category_id, title_de, title_ru, description_de, description_ru , [image,...], [option_id,...], [ingridient_id,...], [<size, price>,....], is_active}
