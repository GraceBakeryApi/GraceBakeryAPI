package cohort46.gracebakeryapi.bakery.ingredient.dao;

import cohort46.gracebakeryapi.bakery.ingredient.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Stream<Ingredient> findByProductsId(Long product_id);
}