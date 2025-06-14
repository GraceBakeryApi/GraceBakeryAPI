package cohort46.gracebakeryapi.bakery.product.controller;

import cohort46.gracebakeryapi.accounting.model.RoleEnum;
import cohort46.gracebakeryapi.accounting.security.UserDetailsImpl;
import cohort46.gracebakeryapi.bakery.product.dto.ProductDto;
import cohort46.gracebakeryapi.bakery.product.dto.findProductsByPriceDto;
import cohort46.gracebakeryapi.bakery.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/product")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto addProduct(@RequestBody ProductDto productDto) {
        return productService.addProduct(productDto)  ;
    }//Long

    @GetMapping("/product/{id}")
    public ProductDto findProductById(@PathVariable Long id) {
        ProductDto temp = productService.findProductById(id);
        if (!temp.getIsActive()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof UserDetailsImpl userDetails) {
                RoleEnum role = userDetails.getUser().getRole();
                if (role != RoleEnum.ADMIN && role != RoleEnum.ROOT) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
            }
        }
        return temp;
    }

    @PutMapping("/product/{id}")
    public ProductDto updateProduct( @RequestBody ProductDto productDto, @PathVariable Long id) {
        return productService.updateProduct(productDto, id);
    }
    @PatchMapping("/product/{id}/isactive/{isactive}")
    ProductDto activateProduct(@PathVariable Long id, @PathVariable Boolean isactive){
        return productService.activateProduct( id, isactive );
    }

    //добавить добавление и удаление сущностей в списках(ингридиенты, фильтры...)

    @GetMapping("/products/isactive/{isactive}")
    public Iterable<ProductDto> findProductsByIsActive(@PathVariable Boolean isactive) {
        return productService.findProductsByIsActive(isactive);
    }

    @GetMapping("/products/all/category/{category_id}")
    public Iterable<ProductDto> findProductsByCategory(@PathVariable Long category_id) {
        return productService.findProductsByCategory(category_id);
    }

    @GetMapping("/products/category/{category_id}")//IsActive = true
    public Iterable<ProductDto> findProductsByCategoryWithoutNoActive(@PathVariable Long category_id) {
        Set<ProductDto> productDtos = new HashSet<>();
        for(ProductDto temp : productService.findProductsByCategoryIdAndIsActive(category_id, true) )
        {
            productDtos.add(productService.getWithNoactives(temp));
        }
        return productDtos;
    }


    @GetMapping("/products/category/{category_id}/isactive/{isActive}")
    public Iterable<ProductDto> findProductsByCategoryIdAndIsActive(@PathVariable Long category_id, @PathVariable Boolean isActive) {
        return productService.findProductsByCategoryIdAndIsActive(category_id, isActive);
    }

    @GetMapping("/products/filters")//IsActive = true
    public Iterable<ProductDto> findProductsByFilters(@RequestBody Iterable<Long> filtersId) {
        return productService.findProductsByFilters(filtersId);
    }

    @GetMapping("/products")
    public Iterable<ProductDto> getProductsAll() {
        return productService.getProductsAll();
    }

    @GetMapping("/products/price/{min}/{max}")//IsActive = true
    public Iterable<findProductsByPriceDto> findProductsByPrice(@PathVariable Double min, @PathVariable Double max) {
        return productService.findProductsByPrice(min, max);
    }
}