package cohort46.gracebakeryapi.bakery.product.service;

import cohort46.gracebakeryapi.bakery.product.dto.ProductDto;
import cohort46.gracebakeryapi.bakery.product.dto.findProductsByPriceDto;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto);
    ProductDto findProductById(Long productId);
    ProductDto updateProduct(ProductDto productDto, Long id);
    ProductDto activateProduct(Long Id, Boolean activate);
    ProductDto getWithNoactives(ProductDto productDto);
    Iterable<ProductDto> findProductsByIsActive(Boolean isactive);
    Iterable<ProductDto> findProductsByCategory(Long category_id);
    Iterable<ProductDto> getProductsAll();
    Iterable<ProductDto> findProductsByFilters(Iterable<Long> filtersId);
    Iterable<findProductsByPriceDto> findProductsByPrice(Double min, Double max);
    Iterable<ProductDto> findProductsByCategoryIdAndIsActive(Long category_id, Boolean isActive);
}

