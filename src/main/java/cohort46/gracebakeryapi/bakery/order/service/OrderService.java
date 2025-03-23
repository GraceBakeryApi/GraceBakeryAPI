package cohort46.gracebakeryapi.bakery.order.service;


import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.accounting.security.UserDetailsImpl;
import cohort46.gracebakeryapi.bakery.order.dto.OrderDto;
import cohort46.gracebakeryapi.bakery.order.model.Order;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

public interface OrderService {
    OrderDto addOrder(OrderDto orderDto);
    OrderDto copyOrderToCart(Long order_id, UserDetailsImpl principal);
    Order storeOrder(Order order);
    //OrderDto patchOrderViewed(Long id);
    OrderDto findOrderById(Long order_id);
    OrderDto updateOrder(OrderDto orderDto, Long id);
    OrderDto deleteOrder(Long Id);
    OrderDto patchOrderTotal(Long id, Double total);
    OrderDto patchOrderFinalsumm(Long id, Double finalsumm);
    OrderDto patchOrderComment(Long id, String comment);
    OrderDto patchOrderStatus(Long id, String status);
    Iterable<OrderDto> findOrdersByUser(Long user_id);
    Iterable<OrderDto> getOrdersAll();
    Iterable<OrderDto> getOrdersTotalAll();
    Iterable<OrderDto> findOrdersByProductId(Long product_id);
    Iterable<OrderDto> findOrderByAdressId(Long adress_id);

    Order createCart(UserAccount user, Order order);
}