package cohort46.gracebakeryapi.bakery.optionsize.dto;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
public class OptionsizeDto {
    @NotNull
    private Long id;

    private double price;

    @NotNull
    @Setter
    private Long sizeid;
}