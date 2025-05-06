package cohort46.gracebakeryapi.order.orderstatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatusDto {
    private String statusDe;
    private String statusRu;
}