package cohort46.gracebakeryapi.order.order.service;

//import cohort46.gracebakeryapi.bakery.order.controller.OrderController;

import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.dto.UserDto;
import cohort46.gracebakeryapi.accounting.model.RoleEnum;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.security.UserDetailsImpl;
import cohort46.gracebakeryapi.accounting.service.UserService;
import cohort46.gracebakeryapi.order.address.dao.AddressRepository;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dao.BakeryoptionalRepository;
import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.filter.dao.FilterRepository;
import cohort46.gracebakeryapi.bakery.ingredient.dao.IngredientRepository;
import cohort46.gracebakeryapi.order.order.dao.OrderRepository;
import cohort46.gracebakeryapi.order.order.dto.OrderDto;
import cohort46.gracebakeryapi.order.order.model.Order;
import cohort46.gracebakeryapi.order.orderitem.dao.OrderitemRepository;
import cohort46.gracebakeryapi.order.orderitem.dto.OrderitemDto;
import cohort46.gracebakeryapi.order.orderitem.model.Orderitem;
import cohort46.gracebakeryapi.order.orderitem.service.OrderitemService;
import cohort46.gracebakeryapi.order.orderitem.service.OrderitemServiceImpl;
import cohort46.gracebakeryapi.bakery.product.dao.ProductRepository;
import cohort46.gracebakeryapi.bakery.productsize.model.Productsize;
import cohort46.gracebakeryapi.bakery.size.dao.SizeRepository;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import cohort46.gracebakeryapi.other.exception.*;
import cohort46.gracebakeryapi.other.helperclasses.GlobalVariables;
import cohort46.gracebakeryapi.order.orderstatus.OrderStatus;
import cohort46.gracebakeryapi.order.orderstatus.OrderStatusDto;
import cohort46.gracebakeryapi.order.orderstatus.OrdersStatusEnum;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

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
    private final UserService userService;
    private final OrderitemServiceImpl orderitemServiceImpl;


    @Transactional
    @Override
    public OrderDto addOrder(OrderDto orderDto) {
        if(  (orderDto.getUserId() == null) || (!checkSource(orderDto)) ) { throw new FailedDependencyException("Creation failed"); };
        UserAccount user = userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new UserNotFoundException(orderDto.getUserId()));
        return modelMapper.map(createCart(user, modelMapper.map(orderDto, Order.class)), OrderDto.class);
    }

    @Transactional
    @Override
    public OrderDto addOrderByAdmin(OrderDto orderDto, String mail, String phone) {
        Optional<UserAccount> userOptional = userRepository.findUserByEmail(mail);
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findAll().stream()
                    .filter(u -> u.getPhone().contains(phone.substring(1))) //remoove '0' or '+'
                    .findFirst();
            if (userOptional.isEmpty()){
                UserDto userDto = new UserDto();
                userDto.setLogin("user_" + Instant.now().toEpochMilli());
                userDto.setPassword("user");
                userDto.setEmail(mail);
                userDto.setPhone(phone);
                userDto.setRole_Id((long)RoleEnum.GUEST.ordinal());
                userDto.setBirthdate(0);
                userOptional = userRepository.findById(userService.addUser(userDto).getId());
            }
        }

        //////////

        OrderStatusDto orderStatusDto = new OrderStatusDto();
        orderStatusDto.setStatusDe(GlobalVariables.getStatusList().get(OrdersStatusEnum.Created.ordinal()).getStatusDe());
        orderStatusDto.setStatusRu(GlobalVariables.getStatusList().get(OrdersStatusEnum.Created.ordinal()).getStatusRu());


        orderDto.setUserId(userOptional.get().getId());
        return modelMapper.map(createOrder(orderDto), OrderDto.class);
    }

    @Transactional
    @Override
    public Order storeOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order createOrder(OrderDto orderDto) {
        Set<OrderitemDto> orderitemDtoSet = orderDto.getOrderitems();
        orderDto.getOrderitems().clear();
        Order order = modelMapper.map(orderDto, Order.class);
        order.getOrderitems().clear();
        orderRepository.saveAndFlush(order);

        for(OrderitemDto orderitemDto:orderitemDtoSet)
        {
            Orderitem orderitem = orderitemRepository.findById(orderitemService.addOrderitemDto(orderitemDto).getId())
                    .orElseThrow(() -> new OrderitemNotFoundException(orderitemDto.getId()));
            order.getOrderitems().add(orderitem);
            orderRepository.saveAndFlush(order);
        }
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
        if(!order.getUser().getId().equals(principal.getUser().getId())) {throw new OrderNotFoundException(order_id);}

        Order cart = orderRepository.findById(principal.getUser().getCartId()).orElseThrow(() -> new OrderNotFoundException(principal.getUser().getCartId()));

        for(Orderitem orderitem : order.getOrderitems()) {
            // very important to Product of the orderitem with general fetcher(Ingredient) should be ordered
            if(orderitem.getProduct().getIsActive()  //the Product must be actuality active
                    && orderitem.getIngredient().getIsActive() //the Ingredient must be actuality active
                    && (orderitem.getProduct().getProductsizes() != null && !orderitem.getProduct().getProductsizes().isEmpty())//currently the Product have any size
                    && orderitem.getProduct().getIngredients().stream().anyMatch( i -> i.getId().equals( orderitem.getIngredient().getId() ) ) //actuality the Product must have this Ingredient
            )
            {
                Size size = orderitem.getSize();
                if(  !orderitem.getSize().getIsActive() ||  orderitem.getProduct().getProductsizes().stream() // is the Product actuality has size whith is size of the orderitem
                        .noneMatch(ps -> ps.getSize().getId().equals(orderitem.getSize().getId()))  ) //if not - find nearest size
                {
                    size = orderitem.getProduct().getProductsizes().stream()
                            .filter(p -> p.getSize() != null)
                            .min(Comparator.comparingDouble(p -> Math.abs(p.getSize().getMass() - orderitem.getSize().getMass())))
                            .map(Productsize::getSize)
                            .orElse(orderitem.getSize());
                };

                //orderitem.getBakeryoptionals().stream().anyMatch(bo -> bo.getOptionsizes().stream().anyMatch(os -> os.getSize().getId().equals(size.getId())))) ) {};

                Orderitem orderitemTemp = modelMapper.map(orderitem, Orderitem.class);
                orderitemTemp.setSize(size);

                orderitemTemp.getBakeryoptionals().clear();//заполнить актуальными
                Long sizeId = size.getId();
                for(Bakeryoptional bakeryoptional : orderitem.getBakeryoptionals()) { //get each bakeryoptional of list of bakeryoptionals from orderitem
                    if( bakeryoptional.getIsActive() && //is this bakeryoptional actuality active
                            (bakeryoptional.getOptionsizes() != null) && !bakeryoptional.getOptionsizes().isEmpty() &&
                            bakeryoptional.getOptionsizes().stream().anyMatch(os -> os.getSize().getId().equals(sizeId)) //is product from orderitem actuality has this bakeryoptional
                    )
                    {
                        orderitemTemp.getBakeryoptionals().add(bakeryoptional);
                    }
                }
                cart.getOrderitems().add(orderitemService.addOrderitem(orderitemTemp, cart));
            }
        }
        return (modelMapper.map(orderRepository.save(cart), OrderDto.class));
    };

    @Transactional
    @Override
    public OrderDto mergeCartfromGuest(Long guest_id, UserDetailsImpl principal){
        UserAccount guest = userRepository.findById(guest_id).orElseThrow(() -> new UserNotFoundException(guest_id));
        Order guestCart = orderRepository.findById(guest.getCartId()).orElseThrow(() -> new OrderNotFoundException(guest.getCartId()));
        Order userCart = createCart(principal.getUser(), new Order());
        Set<Orderitem> orderitemsTemp = new HashSet<>(guestCart.getOrderitems());
        guestCart.getOrderitems().clear();
        userCart.setComment(guestCart.getComment());
        orderRepository.saveAndFlush(guestCart);
        for(Orderitem orderitem : orderitemsTemp ){
           orderitem.setOrder(userCart);
           orderitemRepository.saveAndFlush(orderitem);
           userCart.getOrderitems().add(orderitem);
           orderRepository.saveAndFlush(userCart);
        }
        return (modelMapper.map(userCart, OrderDto.class));
    }

    @Transactional
    @Override
    public OrderDto replaceCartfromGuest(Long guest_id, UserDetailsImpl principal){
        UserAccount guest = userRepository.findById(guest_id).orElseThrow(() -> new UserNotFoundException(guest_id));
        Order guestCart = orderRepository.findById(guest.getCartId()).orElseThrow(() -> new OrderNotFoundException(guest.getCartId()));
        Order userCart = createCart(principal.getUser(), new Order());
        userCart.getOrderitems().clear();
        userCart.setComment(guestCart.getComment());
        orderRepository.saveAndFlush(userCart);
        Set<Orderitem> orderitemsTemp = new HashSet<>(guestCart.getOrderitems());
        guestCart.getOrderitems().clear();
        orderRepository.saveAndFlush(guestCart);
        for(Orderitem orderitem : orderitemsTemp ){
            orderitem.setOrder(userCart);
            orderitemRepository.saveAndFlush(orderitem);
            userCart.getOrderitems().add(orderitem);
            orderRepository.saveAndFlush(userCart);
        }
        return (modelMapper.map(userCart, OrderDto.class));
    }


}


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