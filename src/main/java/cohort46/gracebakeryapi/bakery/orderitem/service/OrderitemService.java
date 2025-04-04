package cohort46.gracebakeryapi.bakery.orderitem.service;


import cohort46.gracebakeryapi.bakery.order.model.Order;
import cohort46.gracebakeryapi.bakery.orderitem.dto.OrderitemDto;
import cohort46.gracebakeryapi.bakery.orderitem.model.Orderitem;
import org.springframework.web.bind.annotation.PathVariable;

public interface OrderitemService {
    OrderitemDto addOrderitemDto(OrderitemDto orderitemDto);
    Orderitem addOrderitem(Orderitem sourcer_orderitem, Order order);
    Orderitem storeOrderitem(Orderitem orderitem);
    OrderitemDto findOrderitemById(Long orderitemId);
    OrderitemDto updateOrderitem(OrderitemDto orderitemDto, Long id);
    OrderitemDto deleteOrderitem(Long Id);
    OrderitemDto patchOrderitemCost(Long id, Double cost);
    OrderitemDto patchOrderitemComment(Long id, String comment);
    Iterable<OrderitemDto> findOrderitemsByOrder(Long sectionid);
    Iterable<OrderitemDto> getOrderitemsAll();
    Iterable<OrderitemDto> findOrderitemsByProductId(Long product_id);
}
