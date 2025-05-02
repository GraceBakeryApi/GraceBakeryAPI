package cohort46.gracebakeryapi.other.image.dto;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
public class ImageDto {
    @NotNull
    @Setter
    private Long id;
    @Setter
    private String image;

    @NotNull
    @Setter
    private Long productid;
}