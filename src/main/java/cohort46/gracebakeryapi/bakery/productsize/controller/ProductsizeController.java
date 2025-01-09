package cohort46.gracebakeryapi.bakery.productsize.controller;

import cohort46.gracebakeryapi.bakery.productsize.dto.ProductsizeDto;
import cohort46.gracebakeryapi.bakery.productsize.service.ProductsizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class ProductsizeController {
    private final ProductsizeService productsizeService;

    @PostMapping("/api/productsize")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductsizeDto addProductsize(@RequestBody ProductsizeDto productsizeDto) {
        return productsizeService.addProductsize(productsizeDto)  ;
    }

    @GetMapping("/api/productsize/{id}")
    public ProductsizeDto findProductsizeById(@PathVariable Long id) {
        return productsizeService.findProductsizeById(id);
    }

    @DeleteMapping("/api/productsize/{id}")
    public ProductsizeDto deleteProductsize(@PathVariable Long id) {
        return productsizeService.deleteProductsize(id);
    }

    @PutMapping("/api/productsize/{id}")
    public ProductsizeDto updateProductsize(@RequestBody ProductsizeDto productsizeDto, @PathVariable Long id) {
        return productsizeService.updateProductsize(productsizeDto, id);
    }

    @GetMapping("/api/productsizes")
    public Iterable<ProductsizeDto> getSizesAll() { return productsizeService.getProductsizesAll(); }
}
