package cohort46.gracebakeryapi.helperclasses;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderStatus {
    private OrdersStatusEnum Status;
    private String statusDe;
    private String statusRu;
}