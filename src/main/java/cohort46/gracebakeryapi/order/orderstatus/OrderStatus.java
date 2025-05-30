package cohort46.gracebakeryapi.order.orderstatus;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderStatus {
    private OrdersStatusEnum Status;
    private String statusDe;
    private String statusRu;
}