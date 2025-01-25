package cohort46.gracebakeryapi.configuration;

import cohort46.gracebakeryapi.accounting.dto.UserDto;
import cohort46.gracebakeryapi.accounting.model.User;
import cohort46.gracebakeryapi.bakery.address.dao.AddressRepository;
import cohort46.gracebakeryapi.bakery.address.dto.AddressDto;
import cohort46.gracebakeryapi.bakery.address.model.Address;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import cohort46.gracebakeryapi.bakery.category.model.Category;
import cohort46.gracebakeryapi.bakery.filter.dto.FilterDto;
import cohort46.gracebakeryapi.bakery.filter.model.Filter;
import cohort46.gracebakeryapi.bakery.image.dto.ImageDto;
import cohort46.gracebakeryapi.bakery.image.model.Image;
import cohort46.gracebakeryapi.bakery.ingredient.dto.IngredientDto;
import cohort46.gracebakeryapi.bakery.ingredient.model.Ingredient;
import cohort46.gracebakeryapi.bakery.optionsize.dto.OptionsizeDto;
import cohort46.gracebakeryapi.bakery.optionsize.model.Optionsize;
import cohort46.gracebakeryapi.bakery.order.dao.OrderRepository;
import cohort46.gracebakeryapi.bakery.order.dto.OrderDto;
import cohort46.gracebakeryapi.bakery.order.model.Order;
import cohort46.gracebakeryapi.bakery.product.dto.ProductDto;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import cohort46.gracebakeryapi.bakery.productsize.dto.ProductsizeDto;
import cohort46.gracebakeryapi.bakery.productsize.model.Productsize;
import cohort46.gracebakeryapi.exception.AddressNotFoundException;
import cohort46.gracebakeryapi.exception.ResourceNotFoundException;
import cohort46.gracebakeryapi.helperclasses.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.HashSet;
import java.util.Set;


@Configuration
public class ServiceConfiguration {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    public ServiceConfiguration(OrderRepository orderRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
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






        /***************User************/
        modelMapper.addMappings(new PropertyMap<UserDto, User>() {
            @Override
            protected void configure() {
                //skip(destination.getAddresses());
                //skip(destination.getRole());
            }
        });

        //*
        modelMapper.addMappings(new PropertyMap<User, UserDto>() {
            protected void configure() {
                using(new Converter<Long, OrderDto>() {
                    public OrderDto convert(MappingContext<Long, OrderDto> context) {
                        return modelMapper.map(orderRepository.findById(context.getSource()).orElse(null) , OrderDto.class) ;
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

        return modelMapper;
    }
}