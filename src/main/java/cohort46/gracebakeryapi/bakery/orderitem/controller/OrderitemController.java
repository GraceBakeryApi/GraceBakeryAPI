package cohort46.gracebakeryapi.bakery.orderitem.controller;

import cohort46.gracebakeryapi.bakery.orderitem.dto.OrderitemDto;
import cohort46.gracebakeryapi.bakery.orderitem.service.OrderitemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderitemController {
    private final OrderitemService orderitemService;

    @PostMapping("/orderitem")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderitemDto addOrderitem(@RequestBody OrderitemDto orderitemDto) {
        return orderitemService.addOrderitem(orderitemDto)  ;
    }

    @GetMapping("/orderitem/{id}")
    public OrderitemDto findOrderitemById(@PathVariable Long id) {
        return orderitemService.findOrderitemById(id);
    }

    @PutMapping("/orderitem/{id}")
    public OrderitemDto updateOrderitem( @RequestBody OrderitemDto orderitemDto, @PathVariable Long id) {
        return orderitemService.updateOrderitem(orderitemDto, id);
    }
    @PatchMapping("/orderitem/{id}/cost/{cost}")
    OrderitemDto patchOrderitemCost(@PathVariable Long id, @PathVariable Double cost){
        return orderitemService.patchOrderitemCost(id, cost);
    }

    @DeleteMapping("/orderitem")
    public OrderitemDto deleteOrderitem(@PathVariable Long id) {
        return orderitemService.deleteOrderitem(id)  ;
    }

    @GetMapping("/orderitems/order/{order_id}")
    public Iterable<OrderitemDto> findOrderitemsByOrder(@PathVariable Long order_id) {
        return orderitemService.findOrderitemsByOrder(order_id);
    }

    @GetMapping("/orderitems/product/{product_id}")
    public Iterable<OrderitemDto> findOrderitemsByProductId(@PathVariable Long product_id) {
        return orderitemService.findOrderitemsByProductId(product_id);
    }

    @GetMapping("/orderitems")
    public Iterable<OrderitemDto> getOrderitemsAll() {
        return orderitemService.getOrderitemsAll();
    }
}
