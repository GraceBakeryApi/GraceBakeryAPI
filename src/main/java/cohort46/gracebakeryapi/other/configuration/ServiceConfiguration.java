package cohort46.gracebakeryapi.other.configuration;

import cohort46.gracebakeryapi.accounting.dto.UserDto;
import cohort46.gracebakeryapi.accounting.model.RoleEnum;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.order.address.dao.AddressRepository;
import cohort46.gracebakeryapi.order.address.dto.AddressDto;
import cohort46.gracebakeryapi.order.address.model.Address;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dao.BakeryoptionalRepository;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import cohort46.gracebakeryapi.bakery.category.model.Category;
import cohort46.gracebakeryapi.bakery.filter.dto.FilterDto;
import cohort46.gracebakeryapi.bakery.filter.model.Filter;
import cohort46.gracebakeryapi.order.orderstatus.OrderStatus;
import cohort46.gracebakeryapi.order.orderstatus.OrderStatusDto;
import cohort46.gracebakeryapi.order.orderstatus.OrdersStatusEnum;
import cohort46.gracebakeryapi.other.exception.*;
import cohort46.gracebakeryapi.other.helperclasses.*;
import cohort46.gracebakeryapi.other.image.dto.ImageDto;
import cohort46.gracebakeryapi.other.image.model.Image;
import cohort46.gracebakeryapi.bakery.ingredient.dao.IngredientRepository;
import cohort46.gracebakeryapi.bakery.ingredient.dto.IngredientDto;
import cohort46.gracebakeryapi.bakery.ingredient.model.Ingredient;
import cohort46.gracebakeryapi.bakery.optionsize.dto.OptionsizeDto;
import cohort46.gracebakeryapi.bakery.optionsize.model.Optionsize;
import cohort46.gracebakeryapi.order.order.dao.OrderRepository;
import cohort46.gracebakeryapi.order.order.dto.OrderDto;
import cohort46.gracebakeryapi.order.order.model.Order;
import cohort46.gracebakeryapi.order.orderitem.dto.OrderitemDto;
import cohort46.gracebakeryapi.order.orderitem.model.Orderitem;
import cohort46.gracebakeryapi.bakery.product.dao.ProductRepository;
import cohort46.gracebakeryapi.bakery.product.dto.ProductDto;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import cohort46.gracebakeryapi.bakery.productsize.dto.ProductsizeDto;
import cohort46.gracebakeryapi.bakery.productsize.model.Productsize;
import cohort46.gracebakeryapi.bakery.size.dao.SizeRepository;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;


@Configuration
public class ServiceConfiguration {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final BakeryoptionalRepository bakeryoptionalRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final IngredientRepository ingredientRepository;

    public ServiceConfiguration(OrderRepository orderRepository, AddressRepository addressRepository, BakeryoptionalRepository bakeryoptionalRepository, ProductRepository productRepository, SizeRepository sizeRepository, IngredientRepository ingredientRepository) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.bakeryoptionalRepository = bakeryoptionalRepository;
        this.productRepository = productRepository;
        this.sizeRepository = sizeRepository;
        this.ingredientRepository = ingredientRepository;
    }


    @Bean
    ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        // Настройка, чтобы игнорировать несовпадающие поля
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT) // Можно использовать STANDARD или LOOSE
                .setSkipNullEnabled(true); // Игнорировать null-поля



        /***************Orderitem*************/
        // Создаем маппинг для OrderitemDto -> Orderitem
        modelMapper.addMappings(new PropertyMap<Orderitem, OrderitemDto>() {
            @Override
            protected void configure() {
                // маппинг полей с разными именами
                map(source.getOrder().getId(), destination.getOrderid());
                map(source.getProduct().getId(), destination.getProductid());
                map(source.getSize().getId(), destination.getSizeid());
                map(source.getIngredient().getId(), destination.getIngredientid());
            }
        });

        modelMapper.addMappings(new PropertyMap<Orderitem, OrderitemDto>() {
            protected void configure() {
                using(new Converter<Set<Bakeryoptional>, Set<BakeryoptionalDto>>() {
                    public Set<BakeryoptionalDto> convert(MappingContext<Set<Bakeryoptional>, Set<BakeryoptionalDto>> context) {
                        Set<BakeryoptionalDto> bakeryoptionalDtoSet = new HashSet<>();
                        for (Bakeryoptional bakeryoptional : context.getSource()) {
                            bakeryoptionalDtoSet.add( modelMapper.map(bakeryoptional, BakeryoptionalDto.class));
                            /*
                            BakeryoptionalDto.builder()
                                    .id(bakeryoptional.getId())
                                    .title_de(bakeryoptional.getTitle_de())
                                    .title_ru(bakeryoptional.getTitle_ru())
                                    .description_de( bakeryoptional.getDescription_de())
                             */
                        }
                        return bakeryoptionalDtoSet;
                    };
                }).map(source.getBakeryoptionals(), destination.getBakeryoptionals());
            }
        });

        modelMapper.addMappings(new PropertyMap<OrderitemDto, Orderitem>() {
            protected void configure() {
                using(new Converter<Set<BakeryoptionalDto>, Set<Bakeryoptional>>() {
                    public Set<Bakeryoptional> convert(MappingContext<Set<BakeryoptionalDto>, Set<Bakeryoptional>> context) {
                        Set<Bakeryoptional> bakeryoptionalSet = new HashSet<>();
                        for (BakeryoptionalDto bakeryoptionalDto : context.getSource()) {
                            bakeryoptionalSet.add( bakeryoptionalRepository.findById(bakeryoptionalDto.getId())
                                    .orElseThrow(() -> new BakeryoptionalNotFoundException(bakeryoptionalDto.getId())));
                        }
                        return bakeryoptionalSet;
                    };
                }).map(source.getBakeryoptionals(), destination.getBakeryoptionals());
            }
        });

        modelMapper.addMappings(new PropertyMap<OrderitemDto, Orderitem>() {
            protected void configure() {
                using(new Converter<Long, Order>() {
                    public Order convert(MappingContext<Long, Order> context) {
                        return orderRepository.findById(context.getSource()).orElseThrow(() -> new OrderNotFoundException(context.getSource()));
                    }
                }).map(source.getOrderid(), destination.getOrder());
            }
        });

        modelMapper.addMappings(new PropertyMap<OrderitemDto, Orderitem>() {
            protected void configure() {
                using(new Converter<Long, Product>() {
                    public Product convert(MappingContext<Long, Product> context) {
                        return productRepository.findById(context.getSource()).orElseThrow(() -> new ProductNotFoundException(context.getSource()));
                    }
                }).map(source.getProductid(), destination.getProduct());
            }
        });

        modelMapper.addMappings(new PropertyMap<OrderitemDto, Orderitem>() {
            protected void configure() {
                using(new Converter<Long, Size>() {
                    public Size convert(MappingContext<Long, Size> context) {
                        return sizeRepository.findById(context.getSource()).orElseThrow(() -> new SizeNotFoundException(context.getSource()));
                    }
                }).map(source.getSizeid(), destination.getSize());
            }
        });

        modelMapper.addMappings(new PropertyMap<OrderitemDto, Orderitem>() {
            protected void configure() {
                using(new Converter<Long, Ingredient>() {
                    public Ingredient convert(MappingContext<Long, Ingredient> context) {
                        return ingredientRepository.findById(context.getSource()).orElseThrow(() -> new IngredientNotFoundException(context.getSource()));
                    }
                }).map(source.getIngredientid(), destination.getIngredient());
            }
        });


        /***************Category*************/
        // Создаем маппинг для Category -> CategoryDto
        modelMapper.addMappings(new PropertyMap<Category, CategoryDto>() {
            @Override
            protected void configure() {
                // маппинг полей с разными именами
                map(source.getSection().getId(), destination.getSectionid());
            }
        });

        /***************Filter*************/
        // Создаем маппинг для Filter -> FilterDto
        modelMapper.addMappings(new PropertyMap<Filter, FilterDto>() {
            protected void configure() {
                map(source.getId(), destination.getId());
                using(new Converter<Set<Product>, Set<Long>>() {
                    public Set<Long> convert(MappingContext<Set<Product>, Set<Long>> context) {
                        Set<Long> productIds = new HashSet<>();
                        for (Product product : context.getSource()) {
                            productIds.add(product.getId());
                        }
                        return productIds;
                    }
                }).map(source.getProducts(), destination.getProductid());
            }
        });

        /***************Ingredient*************/
        // Создаем маппинг для Ingredient -> IngredientDto
        modelMapper.addMappings(new PropertyMap<Ingredient, IngredientDto>()  {
            protected void configure() {
                map(source.getId(), destination.getId());
                using(new Converter<Set<Product>, Set<Long>>() {
                    public Set<Long> convert(MappingContext<Set<Product>, Set<Long>> context) {
                        Set<Long> productIds = new HashSet<>();
                        for (Product product : context.getSource()) {
                            productIds.add(product.getId());
                        }
                        return productIds;
                    }
                }).map(source.getProducts(), destination.getProductid());
            }
        });
/*
        // Создаем маппинг для Ingredient -> IngredientDto
        modelMapper.addMappings(new PropertyMap<IngredientDto, Ingredient>()  {
            protected void configure() {
                map().setImage_de(updateFileLink.update(source.getImage_de(), destination.getImage_de()));
                map().setImage_ru(updateFileLink.update(source.getImage_ru(), destination.getImage_ru()));
            }
        });

 */





        /***************Image*************/
        // Создаем маппинг для Image -> ImageDto
        modelMapper.addMappings(new PropertyMap<Image, ImageDto>() {
            @Override
            protected void configure() {
                // маппинг полей с разными именами
                map(source.getProduct().getId(), destination.getProductid());
                //map(source.getImage(), destination.getImage());
            }
        });

        /***************Address*************/
        modelMapper.addMappings(new PropertyMap<Address, AddressDto>() {
            @Override
            protected void configure() {
                map(source.getUser().getId(), destination.getUserid());
                //map(source.getAddress().)
            }
        });

        /***************Optionsize*************/
        modelMapper.addMappings(new PropertyMap<Optionsize, OptionsizeDto>() {
            @Override
            protected void configure() {
                map(source.getSize().getId(), destination.getSizeid());
            }
        });


        /***************Productsize*************/
        modelMapper.addMappings(new PropertyMap<Productsize, ProductsizeDto>() {
            @Override
            protected void configure() {
                map(source.getSize().getId(), destination.getSizeid());
            }
        });



        /***************Bakeryoptional*************/
        modelMapper.addMappings(new PropertyMap<BakeryoptionalDto, Bakeryoptional>() {
            @Override
            protected void configure() {
                skip(destination.getOptionsizes() );
            }
        });
        modelMapper.addMappings(new PropertyMap<Bakeryoptional, BakeryoptionalDto>() {
            protected void configure() {
                using(new Converter<Set<Optionsize>, Set<SizePrice>>() {
                    public Set<SizePrice> convert(MappingContext<Set<Optionsize>, Set<SizePrice>> context) {
                        Set<SizePrice> sizePrices = new HashSet<>();
                        for (Optionsize optionSize : context.getSource()) {
                            // Создаем объект SizePrice с sizeid, равным id объекта Size
                            sizePrices.add(new SizePrice(optionSize.getSize().getId(), optionSize.getSize().getTitle_de(),  optionSize.getSize().getTitle_ru(),
                                    optionSize.getSize().getMass(), optionSize.getSize().getDiameter(), optionSize.getSize().getPersons(),optionSize.getPrice()));
                        }
                        return sizePrices;
                    }
                }).map(source.getOptionsizes(), destination.getSizeprices());

                map(source.getId(), destination.getId());
                using(new Converter<Set<Product>, Set<Long>>() {
                    public Set<Long> convert(MappingContext<Set<Product>, Set<Long>> context) {
                        Set<Long> productIds = new HashSet<>();
                        for (Product product : context.getSource()) {
                            productIds.add(product.getId());
                        }
                        return productIds;
                    }
                }).map(source.getProducts(), destination.getProductid());
            }

        });

        /***************Order************/
        // Создаем маппинг для Order -> Order
        modelMapper.addMappings(new PropertyMap<Order, Order>() {
            @Override
            protected void configure() {
                skip(destination.getOrderstatus() );
                skip(destination.getOrderitems() );
            }
        });
        // Создаем маппинг для Order -> OrderDto
        modelMapper.addMappings(new PropertyMap<Order, OrderDto>() {
            @Override
            protected void configure() {
                map(source.getUser().getId(), destination.getUserId());
                map(source.getOrderstatus().getStatusDe(), destination.getStatus().getStatusDe());
                map(source.getOrderstatus().getStatusRu(), destination.getStatus().getStatusRu());
            }
        });



        modelMapper.addMappings(new PropertyMap<OrderDto, Order>() {
            protected void configure() {
                using(new Converter<AddressDto, Address>() {
                    public Address convert(MappingContext<AddressDto, Address> context) {
                        return addressRepository.findById(context.getSource().getId()).orElseThrow(() -> new AddressNotFoundException(context.getSource().getId()));// orElse(null) ;
                    }
                }).map(source.getAddress(), destination.getAddress());
            }
        });


        modelMapper.addMappings(new PropertyMap<OrderDto, Order>() {
            protected void configure() {
                using(new Converter<OrderStatusDto, OrderStatus>() {
                    public OrderStatus convert(MappingContext<OrderStatusDto, OrderStatus> context) {
                        if(context.getSource() != null) {
                            OrderStatus temp = GlobalVariables.getStatusList().stream().
                                    filter(os -> os.getStatusDe().equals(context.getSource().getStatusDe())  ||
                                            os.getStatusRu().equals(context.getSource().getStatusRu())
                                    ).findFirst().orElseThrow(() -> new ResourceNotFoundException("OrderStatus type is error"));
                            if( temp != GlobalVariables.getStatusList().get(OrdersStatusEnum.Cart.ordinal()) ) {return temp;}
                        }
                        return context.getDestination();
                    }
                }).map(source.getStatus(), destination.getOrderstatus());
            }
        });







        /***************OrderStatus************/






        /***************UserAccount************/

        modelMapper.addMappings(new PropertyMap<UserAccount, UserDto>() {
            protected void configure() {
                using(new Converter<RoleEnum, Long>() {
                    public Long convert(MappingContext<RoleEnum, Long> context) {
                        return Long.valueOf((long) context.getSource().ordinal());
                    }
                }).map(source.getRole(), destination.getRole_Id());
            }
        });

        modelMapper.addMappings(new PropertyMap<UserAccount, UserDto>() {
            @Override
            protected void configure() {
                skip(destination.getPassword());
            }
        });

        modelMapper.addMappings(new PropertyMap<UserDto, UserAccount>() {
            protected void configure() {
                using(new Converter<Long, RoleEnum>() {
                    public RoleEnum convert(MappingContext<Long, RoleEnum> context) {
                        try {
                            return RoleEnum.values()[context.getSource().intValue()];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            throw new FailedDependencyException("error: invalid role specified")  ;
                        }
                    }
                }).map(source.getRole_Id(), destination.getRole());
            }
        });
        //skip(destination.getAddresses());
        //skip(destination.getRole());

        //*
        modelMapper.addMappings(new PropertyMap<UserAccount, UserDto>() {
            protected void configure() {
                using(new Converter<Long, OrderDto>() {
                    public OrderDto convert(MappingContext<Long, OrderDto> context) {
                        if(context.getSource() != null)
                        {
                            Order temp = orderRepository.findById(context.getSource()).orElse(null);
                            if(temp == null){
                                return null;
                            }
                            else {
                                return modelMapper.map( temp , OrderDto.class) ;
                                //return modelMapper.map(orderRepository.findById(context.getSource()).orElseThrow(() -> new OrderNotFoundException(context.getSource())) , OrderDto.class) ;
                            }
                        }
                        else return null;

                    }
                }).map(source.getCartId(), destination.getCart());
            }
        });

//*/



        /***************Product*************/
        modelMapper.addMappings(new PropertyMap<ProductDto, Product>() {
            @Override
            protected void configure() {
                skip(destination.getImages());
                //skip(destination.getIngredients());
                //skip(destination.getBakeryoptionals());
                //skip(destination.getFilters());
                skip(destination.getProductsizes());

                map(source.getCategoryid()  , destination.getCategory().getId() );
            }
        });

        modelMapper.addMappings(new PropertyMap<Product, ProductDto>() {
            protected void configure() {
                using(new Converter<Set<Productsize>, Set<SizePrice>>() {
                    public Set<SizePrice> convert(MappingContext<Set<Productsize>, Set<SizePrice>> context) {
                        Set<SizePrice> sizePrices = new HashSet<>();
                        for (Productsize productsize : context.getSource()) {
                            // Создаем объект SizePrice с sizeid, равным id объекта Size
                            sizePrices.add(new SizePrice(productsize.getSize().getId(), productsize.getSize().getTitle_de(), productsize.getSize().getTitle_ru(),
                                    productsize.getSize().getMass(), productsize.getSize().getDiameter(), productsize.getSize().getPersons(), productsize.getPrice()));
                        }
                        return sizePrices;
                    }
                }).map(source.getProductsizes(), destination.getSizeprices());
            }
        });

        modelMapper.addMappings(new PropertyMap<Product, ProductDto>() {
            @Override
            protected void configure() {
                map(source.getCategory().getId()  , destination.getCategoryid() );
            }
        });



        /***************String*************/
        /*
        modelMapper.addMappings(new PropertyMap<String, String>() {
            protected void configure() {
                using(new Converter<String, String>() {
                    public String convert(MappingContext<String, String> context) {
                        return context.getSource().replaceAll("\\s+", " ").trim();
                    }
                }).map(source, destination);
            }
        });

         */

        Converter<String, String> trimAndNormalizeSpaces = new Converter<>() {
            @Override
            public String convert(MappingContext<String, String> context) {
                if (context.getSource() == null) {
                    return null;
                }
                return context.getSource().replaceAll("\\s+", " ").trim();
            }
        };

        // Добавляем конвертер глобально
        modelMapper.addConverter(trimAndNormalizeSpaces, String.class, String.class);


        return modelMapper;
    }
}