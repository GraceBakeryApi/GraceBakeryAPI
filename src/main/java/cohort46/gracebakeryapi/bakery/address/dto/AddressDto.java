package cohort46.gracebakeryapi.bakery.address.dto;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
public class AddressDto {
    @NotNull
    @Setter
    private Long id;
    private String address;
    private String city;
    private String street;
    private Integer building;
    private String apartment;

    @NotNull
    @Setter
    private Long userid;
}