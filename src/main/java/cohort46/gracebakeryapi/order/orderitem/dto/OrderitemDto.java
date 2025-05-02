package cohort46.gracebakeryapi.order.orderitem.dto;

import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
