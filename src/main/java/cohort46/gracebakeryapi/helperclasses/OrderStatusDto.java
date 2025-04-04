package cohort46.gracebakeryapi.helperclasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatusDto {
    private String statusDe;
    private String statusRu;
}