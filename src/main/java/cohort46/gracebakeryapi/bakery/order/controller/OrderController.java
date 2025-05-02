package cohort46.gracebakeryapi.bakery.order.controller;

import cohort46.gracebakeryapi.accounting.security.UserDetailsImpl;
import cohort46.gracebakeryapi.bakery.image.dto.ImageDto;
import cohort46.gracebakeryapi.bakery.order.dto.OrderDto;
import cohort46.gracebakeryapi.bakery.order.service.OrderService;
import cohort46.gracebakeryapi.helperclasses.GlobalVariables;
import cohort46.gracebakeryapi.helperclasses.OrdersStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping(value = "/order/admin", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto addOrderByAdmin(@RequestBody OrderDto orderDto, @RequestParam("e-mail") String mail, @RequestParam("phone") String phone) {
        return orderService.addOrderByAdmin(orderDto, mail, phone)  ;
    }

    @GetMapping("/order/{id}")
    public OrderDto findOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }

    @PutMapping("/order/{id}")
    public OrderDto updateOrder( @RequestBody OrderDto orderDto, @PathVariable Long id) {
        return orderService.updateOrder(orderDto, id);
    }

    @DeleteMapping("/order/{id}")
    public OrderDto deleteOrder( @PathVariable Long id) {
        return orderService.deleteOrder(id);
    }

    @PatchMapping("/order/{id}/status/{status}")
    OrderDto patchOrderStatus(@PathVariable Long id, @PathVariable String status){
        return orderService.patchOrderStatus(id, status);
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

    @PatchMapping("/order/{id}/viewed")
    OrderDto patchOrderViewed(@PathVariable Long id){
        return orderService.patchOrderStatus(id, "Viewed");
    }

    @GetMapping("/orders/user/{user_id}")
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

    @GetMapping("/me/copyordertocart/{order_id}")
    public OrderDto copyOrderToCart(@PathVariable Long order_id, @AuthenticationPrincipal UserDetailsImpl principal) {
        return orderService.copyOrderToCart(order_id, principal);
    }

    @GetMapping("/me/mergecartfromguest/{guest_id}")
    public OrderDto mergeCartfromGuest(@PathVariable Long guest_id, @AuthenticationPrincipal UserDetailsImpl principal) {
        return orderService.mergeCartfromGuest(guest_id, principal);
    }

    @GetMapping("/me/replacecartfromguest/{guest_id}")
    public OrderDto replaceCartfromGuest(@PathVariable Long guest_id, @AuthenticationPrincipal UserDetailsImpl principal) {
        return orderService.replaceCartfromGuest(guest_id, principal);
    }

    @GetMapping("/me/cart")
    public OrderDto getMyCart(@AuthenticationPrincipal UserDetailsImpl principal) {
        return orderService.findOrderById(principal.getUser().getCartId());
    }

    @GetMapping("/me/orders")
    public Iterable<OrderDto> getMyOrders(@AuthenticationPrincipal UserDetailsImpl principal) {
        return orderService.findOrdersByUser(principal.getId());
    }


}
