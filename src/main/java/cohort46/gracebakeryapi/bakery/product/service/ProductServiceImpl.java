package cohort46.gracebakeryapi.bakery.product.service;

import cohort46.gracebakeryapi.bakery.bakeryoptional.dao.BakeryoptionalRepository;
import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.bakeryoptional.service.BakeryoptionalService;
import cohort46.gracebakeryapi.bakery.category.dao.CategoryRepository;
import cohort46.gracebakeryapi.bakery.filter.dao.FilterRepository;
import cohort46.gracebakeryapi.bakery.filter.dto.FilterDto;
import cohort46.gracebakeryapi.bakery.filter.model.Filter;
import cohort46.gracebakeryapi.bakery.filter.service.FilterService;
import cohort46.gracebakeryapi.bakery.image.dao.ImageRepository;
import cohort46.gracebakeryapi.bakery.image.dto.ImageDto;
import cohort46.gracebakeryapi.bakery.image.model.Image;
import cohort46.gracebakeryapi.bakery.image.service.ImageService;
import cohort46.gracebakeryapi.bakery.ingredient.dao.IngredientRepository;
import cohort46.gracebakeryapi.bakery.ingredient.dto.IngredientDto;
import cohort46.gracebakeryapi.bakery.ingredient.model.Ingredient;
import cohort46.gracebakeryapi.bakery.ingredient.service.IngredientService;
import cohort46.gracebakeryapi.bakery.product.controller.ProductController;
import cohort46.gracebakeryapi.bakery.product.dao.ProductRepository;
import cohort46.gracebakeryapi.bakery.product.dto.ProductDto;
import cohort46.gracebakeryapi.bakery.product.dto.findProductsByPriceDto;
import cohort46.gracebakeryapi.bakery.productsize.dao.ProductsizeRepository;
import cohort46.gracebakeryapi.bakery.productsize.model.Productsize;
import cohort46.gracebakeryapi.bakery.productsize.service.ProductsizeService;
import cohort46.gracebakeryapi.bakery.size.dao.SizeRepository;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import cohort46.gracebakeryapi.exception.*;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import cohort46.gracebakeryapi.helperclasses.SizePrice;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final ProductsizeRepository productsizeRepository;
    private ProductController productController;

    private final FilterRepository filterRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final IngredientRepository ingredientRepository;
    private final BakeryoptionalRepository bakeryoptionalRepository;

    private final ProductsizeService productsizeService;
    private final FilterService filterService;
    private final IngredientService ingredientService;
    private final BakeryoptionalService bakeryoptionalService;
    private final ImageService imageService;

    private final ModelMapper modelMapper;




    @Transactional
    @Override
    public ProductDto addProduct(ProductDto productDto) {
        if (productDto.getCategoryid() == null) { throw new FailedDependencyException("Category is required ")  ; } ;
        if(!checkSource(productDto)) { throw new FailedDependencyException("Creation failed"); };
        Product product = modelMapper.map(productDto, Product.class);
        product.setId(null);
        product = productRepository.save(product);
        if(product != null) {
            return updateProduct(productDto , product.getId());
        }
        return null;
    }

    @Override
    public ProductDto findProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(  productId   ));
        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional
    @Override
    public ProductDto updateProduct(ProductDto productDto, Long id) {
        productDto.setId(id);
        if(!checkSource(productDto)) throw new FailedDependencyException("Updating failed");
        Product product = productRepository.findById(productDto.getId()).orElseThrow(() -> new ProductNotFoundException(  productDto.getId()   ));

        product.setIsActive(productDto.getIsActive());
        product.setTitle_ru(productDto.getTitle_ru());
        product.setTitle_de(productDto.getTitle_de());
        product.setDescription_de(productDto.getDescription_de());
        product.setDescription_ru(productDto.getDescription_ru());
        product.setTopimage(product.getTopimage());

        if (productDto.getCategoryid() != null) {
            product.setCategory(categoryRepository.findById(productDto.getCategoryid()).orElseThrow(() -> new CategoryNotFoundException(productDto.getCategoryid())));
        }


        //if (productDto.getSizeprices() != null && !productDto.getSizeprices().isEmpty())
        {

            for(Productsize pstemp : product.getProductsizes()){
                productsizeService.deleteProductsize(pstemp.getId());
            }

            product.getProductsizes().clear();
            productRepository.saveAndFlush(product);

            for (SizePrice sizePrice : productDto.getSizeprices()) {
                if( product.getProductsizes().stream().noneMatch(ps -> Objects.equals(ps.getSize().getId(), sizePrice.getSizeid())) ) {
                    Productsize productsize = new Productsize();
                    productsize.setSize(sizeRepository.findById(sizePrice.getSizeid()).orElseThrow(() -> new SizeNotFoundException(sizePrice.getSizeid())));
                    productsize.setPrice(sizePrice.getPrice());
                    productsize.setProduct(product);
                    productsize.setId(null);
                    productsize = productsizeService.store(productsize);
                    product.getProductsizes().add(productsize);
                };
            }
        }

        //if (productDto.getFilters() != null && !productDto.getFilters().isEmpty())
        {
            product.getFilters().forEach(f -> f.getProducts().remove(product));
            product.getFilters().clear();
            for (FilterDto filterDto : productDto.getFilters()) {
                Filter filter = filterRepository.findById(filterDto.getId()).orElseThrow(() -> new FilterNotFoundException(filterDto.getId()));
                filter.getProducts().add(product);
                filterService.store(filter);
                product.getFilters().add(filter);
            }
        }

        //if (productDto.getIngredients() != null && !productDto.getIngredients().isEmpty()) //если в DTO есть запись - то обновляем данные
        {
            product.getIngredients().forEach(i -> i.getProducts().remove(product));//перебираем все ingredient относящиеся к этому product и у каждого ингредиента удаляем из списка продуктов текущий продукт
            product.getIngredients().clear();//очищаем список ингредиентов
            for (IngredientDto ingredientDto : productDto.getIngredients()) {// перебираем все записи в DTO
                Ingredient ingredient = ingredientRepository.findById(ingredientDto.
                        getId()).orElseThrow(() -> new IngredientNotFoundException(ingredientDto.getId())); //находим объект
                ingredient.getProducts().add(product);// добавляем текущий product в поле объекта ingredient
                ingredientService.store(ingredient); //сохраняем ingredient
                product.getIngredients().add(ingredient); //добавляем ingredient в поле product
            }
        }

        //if (productDto.getBakeryoptionals() != null && !productDto.getBakeryoptionals().isEmpty())
        {
            product.getBakeryoptionals().forEach(s -> s.getProducts().remove(product));
            product.getBakeryoptionals().clear();
            for (BakeryoptionalDto bakeryoptionalDto : productDto.getBakeryoptionals()) {
                Bakeryoptional bakeryoptional = bakeryoptionalRepository.findById(bakeryoptionalDto.getId()).orElseThrow(() -> new BakeryoptionalNotFoundException(bakeryoptionalDto.getId()));
                bakeryoptional.getProducts().add(product);
                bakeryoptionalService.store(bakeryoptional);
                product.getBakeryoptionals().add(bakeryoptional);
            }
        }

        //if (productDto.getImages() != null && !productDto.getImages().isEmpty())
        {
            for (ImageDto imageDto : productDto.getImages()) {
                Image image = imageRepository.findById(imageDto.getId()).orElseThrow(() -> new ImageNotFoundException(imageDto.getId()));
                image.setProduct(product);
                imageService.store(image);
            }
        }

        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }


    @Override
    public ProductDto activateProduct(Long productId, Boolean activate) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(  productId   ));
        product.setIsActive(activate);
        return modelMapper.map(productRepository.saveAndFlush(product), ProductDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<ProductDto> findProductsByIsActive(Boolean isactive) {
        return productRepository.findProductsByIsActive(isactive).map(s -> modelMapper.map(s, ProductDto.class)).toList() ;
    }
    @Transactional(readOnly = true)
    @Override
    public Iterable<ProductDto> findProductsByCategory(Long category_id) {
        //сначала выборка по category.id, потом сортировка по полю "ingredients.id" , потом сортировка по полю "productsizes.size.persons"
        categoryRepository.findById(category_id).orElseThrow(() -> new CategoryNotFoundException(  category_id   ));
        Sort sort = Sort.by("ingredients.id").and((Sort.by("productsizes.size.persons")));
        return productRepository.findProductsByCategoryId( category_id  , sort).stream().map(p -> modelMapper.map(p, ProductDto.class)).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<ProductDto> findProductsByCategoryIdAndIsActive(Long category_id, Boolean isactive) {
        //сначала выборка по category.id, потом сортировка по полю "ingredients.id" , потом сортировка по полю "productsizes.size.persons"
        categoryRepository.findById(category_id).orElseThrow(() -> new CategoryNotFoundException(  category_id   ));
        Sort sort = Sort.by("ingredients.id").and((Sort.by("productsizes.size.persons")));
        return productRepository.findProductsByCategoryIdAndIsActive( category_id  , isactive , sort).stream().map(p -> modelMapper.map(p, ProductDto.class)).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<ProductDto> getProductsAll() {
        //сортировка сначала по category.id, потом сортировка по полю "ingredients.id" , потом сортировка по полю "productsizes.size.persons"
        Sort sort = Sort.by("category.id").and(Sort.by("ingredients.id").and((Sort.by("productsizes.size.persons"))));
        return productRepository.findAll(sort).stream().map(p -> modelMapper.map(p, ProductDto.class)).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<ProductDto> findProductsByFilters(Iterable<Long> filtersId) {
        Set<Product> products = new LinkedHashSet<>();
        //filtersId.forEach(f -> products.addAll(filterRepository.findById(f).orElseThrow(ResourceNotFoundException::new).getProducts()));
        filtersId.forEach(f -> filterRepository.findById(f).orElseThrow(() -> new FilterNotFoundException(f)).getProducts().forEach(p -> products.add(p) ) );
        //отсортировать по двум параметрам, во-первых по product.getcategory().getId() , во-вторых по product.getId()
        Set<Product> sortedProducts = products.stream().sorted(Comparator.comparing((Product product) -> product.getCategory().getId()).thenComparing(Product::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortedProducts.stream().map(p -> modelMapper.map(p, ProductDto.class)).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<findProductsByPriceDto> findProductsByPrice(Double min, Double max) {
        List<findProductsByPriceDto> findProductsByPriceDtos = new ArrayList<>();
        List<Product> products = productRepository.findProductsByIsActive(true).toList();
        for(Product product : products) {
            for (Productsize productsize : product.getProductsizes() ) {
                if ( (productsize.getPrice() >= min) && (productsize.getPrice() <= max ) ){
                    findProductsByPriceDtos.add(new findProductsByPriceDto(
                            product.getId(),
                            product.getTitle_de(),
                            product.getTitle_ru(),
                            product.getDescription_de(),
                            product.getDescription_ru(),
                            product.getIsActive(),
                            product.getCategory().getId(),
                            sizeRepository.findById(productsize.getSize().getId()).orElseThrow(() -> new SizeNotFoundException(productsize.getSize().getId())).
                            getPersons(),
                            productsize.getPrice(),
                            product.getTopimage()
                    ));
                }
            }
        }
        return findProductsByPriceDtos;
    }

    @Override
    public ProductDto getWithNoactives(ProductDto productDto) {
        ProductDto newProductDto = modelMapper.map(productDto, ProductDto.class);

        newProductDto.getBakeryoptionals().clear();
        newProductDto.getBakeryoptionals().addAll(productDto.getBakeryoptionals().stream().filter(BakeryoptionalDto::getIsActive).toList());

        newProductDto.getSizeprices().clear();
        newProductDto.getSizeprices().addAll(
                productDto.getSizeprices().stream().filter(s ->
                        sizeRepository.findById(s.getSizeid()).orElseThrow(() -> new SizeNotFoundException(s.getSizeid())).getIsActive()
                ).toList()
        );

        newProductDto.getIngredients().clear();
        newProductDto.getIngredients().addAll(productDto.getIngredients().stream().filter(IngredientDto::getIsActive).toList());

        return newProductDto;
    }

    private boolean checkSource(ProductDto productDto)
    {
        //if ((productDto.getCategoryid() == null) || (categoryRepository.findById(productDto.getCategoryid()).isEmpty())) { return false; }

        if ((productDto.getCategoryid() != null) && categoryRepository.findById(productDto.getCategoryid()).isEmpty()) { return false; }

        if(productRepository.findAll().stream().anyMatch( p -> (p.getTitle_de().equals(productDto.getTitle_de()) &&
                !p.getId().equals(productDto.getId())  )  ) )
        { throw new FailedDependencyException("Title De must be uniq ") ;};


        if(productRepository.findAll().stream().anyMatch( p -> (p.getTitle_ru().equals(productDto.getTitle_ru()) &&
                !p.getId().equals(productDto.getId())  )  ) )
        { throw new FailedDependencyException("Title Ru must be uniq ") ;};


        for (ImageDto imageDto : productDto.getImages()) {
            if(imageRepository.findById(imageDto.getId()).isEmpty()) return false;
        };
        for (IngredientDto ingredientDto : productDto.getIngredients()) {
            if(ingredientRepository.findById(ingredientDto.getId()).isEmpty()) return false;
        };
        for (BakeryoptionalDto bakeryoptionalDto : productDto.getBakeryoptionals()) {
            if(bakeryoptionalRepository.findById(bakeryoptionalDto.getId()).isEmpty()) return false;
        };
        for (FilterDto filterDto : productDto.getFilters()) {
            if(filterRepository.findById(filterDto.getId()).isEmpty()) return false;
        };
        for (SizePrice sizePrice : productDto.getSizeprices()) {
            if(sizeRepository.findById(sizePrice.getSizeid()).isEmpty()) return false;
        };
        /*
        for (ImageDto imageDto : productDto.getImages()) {
            if(imageRepository.findById(imageDto.getId()).isEmpty()) return false;
        };
         */
        return true;
    };

}

