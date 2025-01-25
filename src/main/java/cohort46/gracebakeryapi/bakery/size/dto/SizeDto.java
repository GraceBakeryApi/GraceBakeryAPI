package cohort46.gracebakeryapi.bakery.size.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SizeDto {
    @NotNull
    @Setter
    private Long id;
    private String title_de;
    private String title_ru;
    private Double mass;
    private Integer diameter;
    private Integer persons;

    private Boolean isActive;
}

// {size_id* , title_de, title_ru, mass, diameter, persons}