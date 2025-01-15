package cohort46.gracebakeryapi.accounting.dto;

import cohort46.gracebakeryapi.bakery.address.dto.AddressDto;
import cohort46.gracebakeryapi.bakery.address.model.Address;
//import cohort46.gracebakeryapi.bakery.order.model.Order;
import cohort46.gracebakeryapi.bakery.order.dto.OrderDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Set;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    @NotNull
    @Setter
    private Long id;
    private Set<Long> role_Id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private long birthdate;
    private Set<AddressDto> addresses;
    //private Set<OrderDto> orders;
    private OrderDto cart;
}