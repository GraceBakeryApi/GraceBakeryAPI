package cohort46.gracebakeryapi.bakery.order.dto;

import cohort46.gracebakeryapi.bakery.address.dto.AddressDto;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.orderitem.dto.OrderitemDto;
import cohort46.gracebakeryapi.helperclasses.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Set;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {
    @NotNull
    @Setter
    private Long id;

    private Long userId;
    private Set<OrderitemDto> orderitems; // Здесь можно добавить OrderitemDto, если нужно передавать данные об элементах заказа
    private Double total;
    private String date;
    private AddressDto address;
    private OrderStatus orderstatus;
    private Double finalSum;
    private String comment;
}