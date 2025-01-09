package cohort46.gracebakeryapi.bakery.ingredient.controller;

import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.category.dto.CategoryDto;
import cohort46.gracebakeryapi.bakery.ingredient.dto.IngredientDto;
import cohort46.gracebakeryapi.bakery.ingredient.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @PostMapping("/api/ingredient")
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientDto addIngredient(@RequestBody IngredientDto ingredientDto) {
        return ingredientService.addIngredient(ingredientDto)  ;
    }

    @GetMapping("/api/ingredient/{id}")
    public IngredientDto findIngredientById(@PathVariable Long id) {
        return ingredientService.findIngredientById(id);
    }

    @PutMapping("/api/ingredient/{id}")
    public IngredientDto updateIngredient(@RequestBody IngredientDto ingredientDto, @PathVariable Long id) {
        return ingredientService.updateIngredient(ingredientDto, id);
    }

    @GetMapping("/api/ingredients")
    public Iterable<IngredientDto> getIngredientAll() {
        return ingredientService.getIngredientAll();
    }

    @GetMapping("/api/ingredients/product/{product_id}")
    public Iterable<IngredientDto> findIngredientsByProduct(@PathVariable Long product_id) {
        return ingredientService.findIngredientsByProduct(product_id);
    }
}
