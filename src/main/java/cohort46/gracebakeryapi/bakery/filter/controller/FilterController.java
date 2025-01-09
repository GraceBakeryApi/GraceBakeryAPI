package cohort46.gracebakeryapi.bakery.filter.controller;

import cohort46.gracebakeryapi.bakery.filter.dto.FilterDto;
import cohort46.gracebakeryapi.bakery.filter.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class FilterController {
    private final FilterService filterService;

    @PostMapping("/api/filter")
    @ResponseStatus(HttpStatus.CREATED)
    public FilterDto addFilter(@RequestBody FilterDto filterDto) {
        return filterService.addFilter(filterDto)  ;
    }

    @GetMapping("/api/filter/{id}")
    public FilterDto findFilterById(@PathVariable Long id) {
        return filterService.findFilterById(id);
    }

    @PutMapping("/api/filter/{id}")
    public FilterDto updateFilter(@RequestBody FilterDto filterDto, @PathVariable Long id) {
        return filterService.updateFilter(filterDto, id);
    }

    @GetMapping("/api/filters")
    public Iterable<FilterDto> getFilterAll() {
        return filterService.getFilterAll();
    }

    @GetMapping("/api/filters/product/{product_id}")
    public Iterable<FilterDto> findFiltersByProduct(@PathVariable Long product_id) {
        return filterService.findFiltersByProduct(product_id);
    }
}
