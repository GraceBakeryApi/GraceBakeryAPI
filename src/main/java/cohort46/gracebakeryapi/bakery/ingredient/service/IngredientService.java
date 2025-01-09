package cohort46.gracebakeryapi.bakery.ingredient.service;

import cohort46.gracebakeryapi.bakery.ingredient.dto.IngredientDto;
import cohort46.gracebakeryapi.bakery.ingredient.model.Ingredient;
import org.springframework.web.bind.annotation.PathVariable;


public interface IngredientService {
    IngredientDto addIngredient(IngredientDto ingredientDto);//Long
    IngredientDto findIngredientById(Long ingredientId);
    IngredientDto updateIngredient(IngredientDto ingredientDto, Long id);
    Iterable<IngredientDto> getIngredientAll();
    Iterable<IngredientDto> findIngredientsByProduct(Long product_id);
    Ingredient store(Ingredient ingredient);
}
