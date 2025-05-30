package cohort46.gracebakeryapi.bakery.bakeryoptional.dto;

import cohort46.gracebakeryapi.other.helperclasses.SizePrice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class BakeryoptionalDto {
    @NotNull
    @Setter
    private Long id;
    private String title_de;
    private String title_ru;
    private String description_de;
    private String description_ru;
    @NonNull
    @Setter
    private String image;

    //private Set<ProductDto> products;
    private Set<Long> productid  = new HashSet<>();;

    @JsonProperty("sizeprices")
    private Set<SizePrice> sizeprices =  new HashSet<>();

    private Boolean isActive;
}
