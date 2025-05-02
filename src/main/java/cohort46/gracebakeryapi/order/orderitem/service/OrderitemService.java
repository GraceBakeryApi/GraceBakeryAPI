package cohort46.gracebakeryapi.order.orderitem.service;


import cohort46.gracebakeryapi.order.order.model.Order;
import cohort46.gracebakeryapi.order.orderitem.dto.OrderitemDto;
import cohort46.gracebakeryapi.order.orderitem.model.Orderitem;

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
