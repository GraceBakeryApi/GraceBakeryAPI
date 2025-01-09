package cohort46.gracebakeryapi.bakery.order.service;

//import cohort46.gracebakeryapi.bakery.order.controller.OrderController;

import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.model.User;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dao.BakeryoptionalRepository;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.filter.dao.FilterRepository;
import cohort46.gracebakeryapi.bakery.ingredient.dao.IngredientRepository;
import cohort46.gracebakeryapi.bakery.order.dao.OrderRepository;
import cohort46.gracebakeryapi.bakery.order.dao.OrderRepository;
import cohort46.gracebakeryapi.bakery.order.dto.OrderDto;
import cohort46.gracebakeryapi.bakery.order.model.Order;
import cohort46.gracebakeryapi.bakery.order.service.OrderService;
import cohort46.gracebakeryapi.bakery.orderitem.dao.OrderitemRepository;
import cohort46.gracebakeryapi.bakery.orderitem.dto.OrderitemDto;
import cohort46.gracebakeryapi.bakery.product.dao.ProductRepository;
import cohort46.gracebakeryapi.bakery.size.dao.SizeRepository;
import cohort46.gracebakeryapi.exception.FailedDependencyException;
import cohort46.gracebakeryapi.exception.OrderNotFoundException;
import cohort46.gracebakeryapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    //private OrderController orderController;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderitemRepository orderitemRepository;
    private final ProductRepository productRepository;
    private final FilterRepository filterRepository;
    private final SizeRepository sizeRepository;
    private final IngredientRepository ingredientRepository;
    private final BakeryoptionalRepository bakeryoptionalRepository;


    @Transactional
    @Override
    public OrderDto addOrder(OrderDto orderDto) {
        if(  (orderDto.getUserId() == null) || (checkSource(orderDto)) ) { throw new FailedDependencyException("Creation failed"); };
        User user = userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new UserNotFoundException(orderDto.getUserId()));
        //user.getOrders().stream().sorted()//если есть корзина то вернуть ее
        Order order = modelMapper.map(orderDto, Order.class);
        order.setId(null);
        orderRepository.save(order);
        userRepository.findById(order.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException(order.getUser().getId()))
                .getOrders().add(order) ;
        return modelMapper.map(order, OrderDto.class);
    }



    @Transactional
    @Override
    public Order storeOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public OrderDto findOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        return modelMapper.map(order, OrderDto.class);
    }

    @Transactional
    @Override
    public OrderDto updateOrder(OrderDto orderDto, Long id) {
        orderDto.setId(id);
        Order order = orderRepository.findById(orderDto.getId()).orElseThrow(() -> new OrderNotFoundException(orderDto.getId()));
        modelMapper.addMappings(new PropertyMap<OrderDto, Order>() {
            @Override
            protected void configure() {
                skip(destination.getUser());  // Игнорируем поле Order
            }
        });
        if(checkSource(orderDto)) { throw new FailedDependencyException("Creation failed"); };/////////////////// надо сделать check без проверки user
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }

    @Override
    public Order deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        //проверить статус order, удалять можно только заказ до состояния "в работе"
        orderRepository.delete(order);
        return order;
    }

    @Transactional
    @Override
    public OrderDto patchOrderTotal(Long id, Double total) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.setTotal(total);
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }

    @Transactional
    @Override
    public OrderDto patchOrderFinalsumm(Long id, Double finalsumm) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.setFinal_sum(finalsumm);
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }

    @Transactional
    @Override
    public OrderDto patchOrderComment(Long id, String comment) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.setComment(comment);
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<OrderDto> findOrdersByUser(Long userid) {
        return orderRepository.findOrdersByUserId(userid, Sort.by("id"))
                .map(order -> modelMapper.map(order, OrderDto.class)).toList();
    }


    @Transactional(readOnly = true)
    @Override
    public Iterable<OrderDto> getOrdersAll() {
        return orderRepository.findAll().stream().map(order -> modelMapper.map(order, OrderDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<OrderDto> findOrdersByProductId(Long product_id) {
        return null;//orderRepository.findOrdersByProductId(product_id, Sort.by("id")).map(order -> modelMapper.map(order, OrderDto.class)).toList()   ;
    }

    private boolean checkSource(OrderDto orderDto)
    {
        //if ((orderDto.getUserId() == null) || (orderRepository.findById(orderDto.getUserId()).isEmpty())) { return false; }
        for (OrderitemDto orderitemDto : orderDto.getOrderitems()) {
            if(orderitemRepository.findById(orderitemDto.getId()).isEmpty()) return false;
        };
        return true;
    };

}