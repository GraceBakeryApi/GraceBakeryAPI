package cohort46.gracebakeryapi.bakery.order.controller;

import cohort46.gracebakeryapi.bakery.order.dto.OrderDto;
import cohort46.gracebakeryapi.bakery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto addOrder(@RequestBody OrderDto orderDto) {
        return orderService.addOrder(orderDto)  ;
    }//Long

    @GetMapping("/order/{id}")
    public OrderDto findOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }

    @PutMapping("/order/{id}")
    public OrderDto updateOrder( @RequestBody OrderDto orderDto, @PathVariable Long id) {
        return orderService.updateOrder(orderDto, id);
    }
    @PatchMapping("/order/{id}/total/{total}")
    OrderDto patchOrderTotal(@PathVariable Long id, @PathVariable Double total){
        return orderService.patchOrderTotal(id, total);
    }

    @PatchMapping("/order/{id}/finalsumm/{finalsumm}")
    OrderDto patchOrderFinalsumm(@PathVariable Long id, @PathVariable Double finalsumm){
        return orderService.patchOrderFinalsumm(id, finalsumm);
    }

    @PatchMapping("/order/{id}/comment/{comment}")
    OrderDto patchOrderComment(@PathVariable Long id, @PathVariable String comment){
        return orderService.patchOrderComment(id, comment);
    }

    @GetMapping("/orders/order/{order_id}")
    public Iterable<OrderDto> findOrdersByUser(@PathVariable Long user_id) {
        return orderService.findOrdersByUser(user_id);
    }

    @GetMapping("/orders/product/{product_id}")
    public Iterable<OrderDto> findOrdersByProductId(@PathVariable Long product_id) {
        return orderService.findOrdersByProductId(product_id);
    }

    @GetMapping("/orders")
    public Iterable<OrderDto> getOrdersAll() {
        return orderService.getOrdersAll();
    }
}
