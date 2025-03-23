package cohort46.gracebakeryapi.bakery.order.service;

//import cohort46.gracebakeryapi.bakery.order.controller.OrderController;

import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.accounting.security.UserDetailsImpl;
import cohort46.gracebakeryapi.bakery.address.dao.AddressRepository;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dao.BakeryoptionalRepository;
import cohort46.gracebakeryapi.bakery.filter.dao.FilterRepository;
import cohort46.gracebakeryapi.bakery.ingredient.dao.IngredientRepository;
import cohort46.gracebakeryapi.bakery.order.dao.OrderRepository;
import cohort46.gracebakeryapi.bakery.order.dto.OrderDto;
import cohort46.gracebakeryapi.bakery.order.model.Order;
import cohort46.gracebakeryapi.bakery.orderitem.dao.OrderitemRepository;
import cohort46.gracebakeryapi.bakery.orderitem.dto.OrderitemDto;
import cohort46.gracebakeryapi.bakery.orderitem.model.Orderitem;
import cohort46.gracebakeryapi.bakery.orderitem.service.OrderitemService;
import cohort46.gracebakeryapi.bakery.product.dao.ProductRepository;
import cohort46.gracebakeryapi.bakery.size.dao.SizeRepository;
import cohort46.gracebakeryapi.exception.*;
import cohort46.gracebakeryapi.helperclasses.GlobalVariables;
import cohort46.gracebakeryapi.helperclasses.OrderStatus;
import cohort46.gracebakeryapi.helperclasses.OrdersStatusEnum;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    //private OrderController orderController;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderitemRepository orderitemRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final FilterRepository filterRepository;
    private final SizeRepository sizeRepository;
    private final IngredientRepository ingredientRepository;
    private final BakeryoptionalRepository bakeryoptionalRepository;

    private final OrderitemService orderitemService;


    @Transactional
    @Override
    public OrderDto addOrder(OrderDto orderDto) {
        if(  (orderDto.getUserId() == null) || (!checkSource(orderDto)) ) { throw new FailedDependencyException("Creation failed"); };
        UserAccount user = userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new UserNotFoundException(orderDto.getUserId()));
        return modelMapper.map(createCart(user, modelMapper.map(orderDto, Order.class)), OrderDto.class);
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
        if(!checkSource(orderDto)) { throw new FailedDependencyException("Creation failed"); };/////////////////// надо сделать check без проверки user
        /*
        modelMapper.addMappings(new PropertyMap<OrderDto, Order>() {
            @Override
            protected void configure() {
                skip(destination.getUser());  // Игнорируем поле UserAccount
            }
        });
         */
        UserAccount tempOrder = order.getUser();
        //OrderStatus tempOrderStatus = order.getOrderstatus();
        modelMapper.map(orderDto, order);
        order.setUser(tempOrder); //сохраняем user таким , какой он был
        orderRepository.saveAndFlush(order);
        //if(tempOrderStatus.getStatus().equals(OrdersStatusEnum.Cart))
        {
            createCart(order.getUser(), new Order());
        }
        return modelMapper.map(order, OrderDto.class);
    }

    @Transactional
    @Override
    public OrderDto deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        //проверить статус order, удалять можно только заказ до состояния "в работе"
        if(  (order.getOrderstatus().getStatus().equals(OrdersStatusEnum.Cart)) ||
                (order.getOrderstatus().getStatus().equals(OrdersStatusEnum.Created)) )
        {
            orderRepository.delete(order);
            return modelMapper.map(order, OrderDto.class);
        }
        else {throw new NotAcceptableException("Deletion is not available at this stage");}
    }

    @Transactional
    @Override
    public OrderDto patchOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        OrderStatus temp = GlobalVariables.getStatusList().stream().filter(os ->
                        os.getStatusDe().equals(status)  ||
                        os.getStatusRu().equals(status)
                ).findFirst().orElseThrow(() -> new ResourceNotFoundException("OrderStatus type is error"));
        order.setOrderstatus(temp);
        orderRepository.saveAndFlush(order);
        createCart(order.getUser(), new Order());
        return modelMapper.map(order, OrderDto.class);
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
    return orderRepository.findAll().stream()
            .filter(order -> !order.getOrderstatus().equals(
                    GlobalVariables.getStatusList().get(OrdersStatusEnum.Cart.ordinal())))
            .sorted(Comparator.comparing((Order order) ->
                    order.getOrderstatus().equals(GlobalVariables.getStatusList().get(OrdersStatusEnum.Created.ordinal())) ? 0 : 1)
                    .thenComparing(Order::getDate))
            .map(order -> modelMapper.map(order, OrderDto.class))
            .toList();
}

    @Transactional(readOnly = true)
    @Override
    public Iterable<OrderDto> getOrdersTotalAll() {
        return orderRepository.findAll().stream().map(order -> modelMapper.map(order, OrderDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<OrderDto> findOrdersByProductId(Long product_id) {
        productRepository.findById(product_id).orElseThrow(() -> new ProductNotFoundException(product_id));
        return orderRepository.findAll().stream().filter(order -> order.getOrderitems().stream()
                .anyMatch(orderitem -> orderitem.getProduct().getId().equals(product_id))
        ).map(order -> modelMapper.map(order, OrderDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<OrderDto> findOrderByAdressId(Long address_id) {
        return  orderRepository.findOrdersByAddress(addressRepository.findById(address_id).orElseThrow(() -> new AddressNotFoundException(address_id))
                , Sort.by("id")).map(order -> modelMapper.map(order, OrderDto.class)).toList();
    }

    private boolean checkSource(OrderDto orderDto)
    {
        //if ((orderDto.getUserId() == null) || (orderRepository.findById(orderDto.getUserId()).isEmpty())) { return false; }
        for (OrderitemDto orderitemDto : orderDto.getOrderitems()) {
            if(orderitemRepository.findById(orderitemDto.getId()).isEmpty()) return false;
        };
        return true;
    };

    @Transactional
    @Override
    public Order createCart(UserAccount user, Order order){
        Optional<Order> temp = user.getOrders().stream().filter(o ->
                o.getOrderstatus().getStatus().equals(OrdersStatusEnum.Cart)).findFirst();//если у user уже есть корзина, то вернуть ее
        if(temp.isPresent()) { return temp.get();}
        else { //если у user нет корзины, то назначить создаваемому order статус корзины
            order.setId(null);
            order.setUser(user);
            order.setOrderstatus(GlobalVariables.getStatusList().get(OrdersStatusEnum.Cart.ordinal()));
            order.setCreatingdate(Instant.now().toEpochMilli());
            orderRepository.saveAndFlush(order);
            user.getOrders().add(order);
            user.setCartId(order.getId());
            userRepository.save(user);
            return order;
        }
    };

    @Transactional
    @Override
    public OrderDto copyOrderToCart(Long order_id, UserDetailsImpl principal){
        //principal.getUser().getOrders().stream().noneMatch(order -> order.getId().equals(order_id));
        Order order = orderRepository.findById(order_id).orElseThrow(() -> new OrderNotFoundException(order_id));
        if(order.getUser().getId().equals(principal.getUser().getId())) {throw new OrderNotFoundException(order_id);}

        Order cart = orderRepository.findById(principal.getUser().getCartId()).orElseThrow(() -> new OrderNotFoundException(principal.getUser().getCartId()));

        for(Orderitem orderitem : order.getOrderitems()) {
            if(orderitem.getProduct().getIsActive()) {
                cart.getOrderitems().add(orderitemService.addOrderitem(orderitem, cart));
            }
        }
        return (modelMapper.map(orderRepository.save(order), OrderDto.class));
    };


}

/*
Order order = new Order();//создаем корзину для создаваемого user
            order.setUser(user);
            order.setOrderstatus(GlobalVariables.getStatusList().get(OrdersStatusEnum.Cart.ordinal()));
            user.getOrders().add(order);
            user.setCartId(orderRepository.saveAndFlush(order).getId());
            user = userRepository.saveAndFlush(user);
            return modelMapper.map(user, UserDto.class);
 */


/*
@Transactional
    @Override
    public OrderDto addOrder(OrderDto orderDto) {
        if(  (orderDto.getUserId() == null) || (!checkSource(orderDto)) ) { throw new FailedDependencyException("Creation failed"); };
        UserAccount user = userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new UserNotFoundException(orderDto.getUserId()));
        Optional<Order> temp = user.getOrders().stream().filter(order ->
                order.getOrderstatus().getStatus().equals(OrdersStatusEnum.Cart)).findFirst();//если у user уже есть корзина, то вернуть ее
        if(temp.isPresent()) { return modelMapper.map(temp.get(), OrderDto.class) ; }
        else {
            //если у user нет корзины, то назначить создаваемому order статус корзины
            Order order = modelMapper.map(orderDto, Order.class);
            createCart(user, order);
            return modelMapper.map(createCart(user, modelMapper.map(orderDto, Order.class)), OrderDto.class);


            createCart(user, order);
            order.setId(null);
            order.setUser(user);
            order.setOrderstatus(GlobalVariables.getStatusList().get(OrdersStatusEnum.Cart.ordinal()));
            orderRepository.save(order);
            user = userRepository.findById(order.getUser().getId()).orElseThrow(() -> new UserNotFoundException(order.getUser().getId()));
            user.getOrders().add(order) ;
            user.setCartId(order.getId());
            userRepository.save(user);
            return modelMapper.map(order, OrderDto.class);


        }
                }
 */