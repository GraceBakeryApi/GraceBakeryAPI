package cohort46.gracebakeryapi.bakery.filter.dto;

import cohort46.gracebakeryapi.bakery.product.dto.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Set;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterDto {
    @NotNull
    @Setter
    private Long id;
    private String title_de;
    private String title_ru;
    private String description_de;
    private String description_ru;
    @Setter
    private String image;


    //@JsonProperty("product")
    private Set<Long> productid;
}

