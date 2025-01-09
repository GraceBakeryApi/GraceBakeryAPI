package cohort46.gracebakeryapi.bakery.productsize.service;

import cohort46.gracebakeryapi.bakery.productsize.dto.ProductsizeDto;
import cohort46.gracebakeryapi.bakery.productsize.model.Productsize;


public interface ProductsizeService {
    ProductsizeDto addProductsize(ProductsizeDto productsizeDto);//Long
    ProductsizeDto findProductsizeById(Long productsizeId);
    ProductsizeDto deleteProductsize(Long id);
    ProductsizeDto updateProductsize(ProductsizeDto productsizeDto, Long id);
    Iterable<ProductsizeDto> getProductsizesAll();
    Productsize store(Productsize productsize);
}
