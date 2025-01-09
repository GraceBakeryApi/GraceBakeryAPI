package cohort46.gracebakeryapi.bakery.bakeryoptional.controller;

import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.bakeryoptional.service.BakeryoptionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class BakeryoptionalController {
    private final BakeryoptionalService bakeryoptionalService;

    @PostMapping("/api/option")
    @ResponseStatus(HttpStatus.CREATED)
    public BakeryoptionalDto addBakeryoptional(@RequestBody BakeryoptionalDto bakeryoptionalDto) {
        return bakeryoptionalService.addBakeryoptional(bakeryoptionalDto)  ;
    }//Long

    @GetMapping("/api/option/{id}")
    public BakeryoptionalDto findBakeryoptionalById(@PathVariable Long id) {
        return bakeryoptionalService.findBakeryoptionalById(id);
    }

    @PutMapping("/api/option/{id}")
    public BakeryoptionalDto updateBakeryoptional(@RequestBody BakeryoptionalDto bakeryoptionalDto, @PathVariable Long id) {
        return bakeryoptionalService.updateBakeryoptional(bakeryoptionalDto, id);
    }

    @GetMapping("/api/options/product/{product_id}")
    public Iterable<BakeryoptionalDto> findBakeryoptionalsByProduct(@PathVariable Long product_id) {
        return bakeryoptionalService.findBakeryoptionalsByProduct(product_id);
    }

    @GetMapping("/api/options")
    public Iterable<BakeryoptionalDto> getBakeryoptionalsAll() {
        return bakeryoptionalService.getBakeryoptionalsAll();
    }
}
