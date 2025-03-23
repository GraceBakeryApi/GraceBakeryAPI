package cohort46.gracebakeryapi.bakery.orderitem.service;

//import cohort46.gracebakeryapi.bakery.orderitem.controller.OrderitemController;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dao.BakeryoptionalRepository;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.filter.dao.FilterRepository;
import cohort46.gracebakeryapi.bakery.filter.dto.FilterDto;
import cohort46.gracebakeryapi.bakery.image.dto.ImageDto;
import cohort46.gracebakeryapi.bakery.ingredient.dao.IngredientRepository;
import cohort46.gracebakeryapi.bakery.ingredient.dto.IngredientDto;
import cohort46.gracebakeryapi.bakery.optionsize.model.Optionsize;
import cohort46.gracebakeryapi.bakery.order.dao.OrderRepository;
import cohort46.gracebakeryapi.bakery.order.model.Order;
import cohort46.gracebakeryapi.bakery.orderitem.dao.OrderitemRepository;
import cohort46.gracebakeryapi.bakery.orderitem.dto.OrderitemDto;
import cohort46.gracebakeryapi.bakery.orderitem.model.Orderitem;
import cohort46.gracebakeryapi.bakery.orderitem.service.OrderitemService;
import cohort46.gracebakeryapi.bakery.product.dao.ProductRepository;
import cohort46.gracebakeryapi.bakery.product.dto.ProductDto;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import cohort46.gracebakeryapi.bakery.productsize.model.Productsize;
import cohort46.gracebakeryapi.bakery.section.dao.SectionRepository;
import cohort46.gracebakeryapi.bakery.section.model.Section;
//import cohort46.gracebakeryapi.exception.EntityNotFoundException;
import cohort46.gracebakeryapi.bakery.size.dao.SizeRepository;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import cohort46.gracebakeryapi.exception.*;
import cohort46.gracebakeryapi.helperclasses.OrdersStatusEnum;
import cohort46.gracebakeryapi.helperclasses.SizePrice;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderitemServiceImpl implements OrderitemService {
    //private OrderitemController orderitemController;

    private final ModelMapper modelMapper;

    private final OrderitemRepository orderitemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final FilterRepository filterRepository;
    private final SizeRepository sizeRepository;
    private final IngredientRepository ingredientRepository;
    private final BakeryoptionalRepository bakeryoptionalRepository;


    @Transactional
    @Override
    public OrderitemDto addOrderitemDto(OrderitemDto orderitemDto) {
        if((orderitemDto.getOrderid() == null) || (orderRepository.findById(orderitemDto.getOrderid()).isEmpty())
                || (checkSource(orderitemDto)) ) { throw new FailedDependencyException("Creation failed"); };
        Orderitem orderitem = modelMapper.map(orderitemDto, Orderitem.class);
        orderitem.setId(null);

        if(orderitemDto.getCost() == null) { orderitem.setCost(  calcucateCost(orderitem) ); };//если orderitemDto.Cost не задано то провести калькуляцию

        orderitemRepository.save(orderitem);
        orderRepository.findById(orderitem.getOrder().getId())
                .orElseThrow(() -> new OrderitemNotFoundException(orderitem.getOrder().getId()))
                .getOrderitems().add(orderitem) ;
        return modelMapper.map(orderitem, OrderitemDto.class);
    }


    @Transactional
    @Override
    public Orderitem addOrderitem(Orderitem sourcer_orderitem, Order order) {
        Orderitem temp = Orderitem.builder()
                .id(null)
                .order(order)
                .product(sourcer_orderitem.getProduct())
                .ingredient(sourcer_orderitem.getIngredient())
                .size(sourcer_orderitem.getSize())
                .bakeryoptionals(sourcer_orderitem.getBakeryoptionals())
                .quantity(sourcer_orderitem.getQuantity())
                .cost(calcucateCost(sourcer_orderitem))
                .comment(sourcer_orderitem.getComment())
                .build();
        return orderitemRepository.saveAndFlush(temp);
    };



    @Transactional
    @Override
    public Orderitem storeOrderitem(Orderitem orderitem) {
        return orderitemRepository.save(orderitem);
    }

    @Override
    public OrderitemDto findOrderitemById(Long orderitemId) {
        Orderitem orderitem = orderitemRepository.findById(orderitemId).orElseThrow(() -> new OrderitemNotFoundException(orderitemId));
        return modelMapper.map(orderitem, OrderitemDto.class);
    }

    @Transactional
    @Override
    public OrderitemDto updateOrderitem(OrderitemDto orderitemDto, Long id) {
        orderitemDto.setId(id);
        Orderitem orderitem = orderitemRepository.findById(orderitemDto.getId()).orElseThrow(() -> new OrderitemNotFoundException(orderitemDto.getId()));
        modelMapper.addMappings(new PropertyMap<OrderitemDto, Orderitem>() {
            @Override
            protected void configure() {
                skip(destination.getOrder());  // Игнорируем поле Order
            }
        });
        if(checkSource(orderitemDto)) { throw new FailedDependencyException("Creation failed"); };///////////////////
        if(orderitemDto.getCost() == null) { orderitem.setCost(  calcucateCost(orderitem) ); };//если orderitemDto.Cost не задано то провести калькуляцию
        return modelMapper.map(orderitemRepository.save(orderitem), OrderitemDto.class);
    }

    @Override
    public OrderitemDto deleteOrderitem(Long id) {
        Orderitem orderitem = orderitemRepository.findById(id).orElseThrow(() -> new OrderitemNotFoundException(id));
        //проверить статус order, удалять можно только заказ до состояния "в работе"
        if(  ( orderitem.getOrder() != null ) &&
                ( orderitem.getOrder().getOrderstatus().getStatus() != OrdersStatusEnum.Cart ) &&
                        ( orderitem.getOrder().getOrderstatus().getStatus() != OrdersStatusEnum.Created )
        )  { throw new NotAcceptableException("Deletion is not available at this stage");  }

        orderitemRepository.delete(orderitem);
        return modelMapper.map(orderitem, OrderitemDto.class);
    }

    @Transactional
    @Override
    public OrderitemDto patchOrderitemCost(Long id, Double cost) {
        Orderitem orderitem = orderitemRepository.findById(id).orElseThrow(() -> new OrderitemNotFoundException(id));
        orderitem.setCost(cost);
        return modelMapper.map(orderitemRepository.save(orderitem), OrderitemDto.class);
    }

    @Transactional
    @Override
    public OrderitemDto patchOrderitemComment(Long id, String comment) {
        Orderitem orderitem = orderitemRepository.findById(id).orElseThrow(() -> new OrderitemNotFoundException(id));
        orderitem.setComment(comment);
        return modelMapper.map(orderitemRepository.save(orderitem), OrderitemDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<OrderitemDto> findOrderitemsByOrder(Long orderid) {
        return orderitemRepository.findOrderitemsByOrderId(orderid, Sort.by("id"))
                .map(orderitem -> modelMapper.map(orderitem, OrderitemDto.class)).toList();
    }


    @Transactional(readOnly = true)
    @Override
    public Iterable<OrderitemDto> getOrderitemsAll() {
        return orderitemRepository.findAll().stream().map(orderitem -> modelMapper.map(orderitem, OrderitemDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<OrderitemDto> findOrderitemsByProductId(Long product_id) {
        return orderitemRepository.findOrderitemsByProductId(product_id, Sort.by("id")).map(orderitem -> modelMapper.map(orderitem, OrderitemDto.class)).toList()   ;
    }

    private boolean checkSource(OrderitemDto orderitemDto)
    {
        //if ((orderitemDto.getOrderid() == null) || (orderRepository.findById(orderitemDto.getOrderid()).isEmpty())) { return false; }
        if ((orderitemDto.getProductid() == null) || (productRepository.findById(orderitemDto.getProductid()).isEmpty())) { return false; }

        /*
        for (Productsize productsize : productRepository.findById(orderitemDto.getProductid()).get().getProductsizes())
        {
        }
         */

        Product product = productRepository.findById(orderitemDto.getProductid()).get();

        if( product.getProductsizes().stream().noneMatch(ps -> ps.getSize().getId().equals(orderitemDto.getSizeid()))  ) {return false; }  // имеет ли product такой size

        for (BakeryoptionalDto bakeryoptionalDto : orderitemDto.getBakeryoptionals()) {
            if(bakeryoptionalRepository.findById(bakeryoptionalDto.getId()).isEmpty()) {return false;};

            if( bakeryoptionalRepository.findById(bakeryoptionalDto.getId()).get().getOptionsizes().stream()  //проверить применим ли к этому bakeryoptional текущий size
                    .noneMatch(os -> os.getSize().getId().equals(orderitemDto.getSizeid()))  ) {return false; }

            if( product.getBakeryoptionals().stream().noneMatch(ps -> ps.getId().equals(bakeryoptionalDto.getId()))  ) {return false; } //проверить применим ли этот bakeryoptional к этому product

        };

        if ((orderitemDto.getSizeid() == null) || (sizeRepository.findById(orderitemDto.getSizeid()).isEmpty())) { return false; };
        if ((orderitemDto.getIngredientid() == null) || (ingredientRepository.findById(orderitemDto.getIngredientid()).isEmpty())) { return false; }

        if(  (orderitemDto.getQuantity() == null) || (orderitemDto.getQuantity() > 0) ) { return false; } // колличество должно быть , и оно должно быть больше 0

        return true;
    };
//*
    private Double calcucateCost(Orderitem orderitem) {
        if(!orderitem.getProduct().getIsActive()) { throw new FailedDependencyException("product is disabled"); }

        double cost = 0;
        Optional<Double> temp;

        temp = orderitem.getProduct().getProductsizes().stream().filter(ps -> ps.getSize().getId().equals(orderitem.getSize().getId())).
                findFirst().map(Productsize::getPrice) ; // temp = ценна продукта для этого размера, если этот размер относится к этому продукту
        if (temp.isPresent()) { cost = temp.get(); }
        else { throw new FailedDependencyException("Creation failed, no size or price in product"); };

        for(Bakeryoptional bo : orderitem.getBakeryoptionals())
        {
            temp = bo.getOptionsizes().stream().filter(os -> os.getSize().getId().equals(orderitem.getSize().getId())).
                    findFirst().map(Optionsize::getPrice) ;//temp = ценна очередной опции для этого размера, если этот размер относится к этой опции
            if (temp.isPresent() && bo.getIsActive()) { cost = cost + temp.get(); }
            else { throw new FailedDependencyException("Creation failed, no size or price in option"); };
        }

        cost = cost*orderitem.getQuantity();

        return cost;
    };
//*/


}