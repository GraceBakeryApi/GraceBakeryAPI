package cohort46.gracebakeryapi.order.order.dto;

import cohort46.gracebakeryapi.order.address.dto.AddressDto;
import cohort46.gracebakeryapi.order.orderitem.dto.OrderitemDto;
import cohort46.gracebakeryapi.other.helperclasses.OrderStatusDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {
    @NotNull
    @Setter
    private Long id;
    @Setter
    private Long userId;
    private Set<OrderitemDto> orderitems = new HashSet<>(); // Здесь можно добавить OrderitemDto, если нужно передавать данные об элементах заказа
    private Double total;
    private String date;
    private Long creatingdate;
    private AddressDto address;
    @Setter
    private OrderStatusDto status;
    private Double final_sum;
    private String comment;
}
