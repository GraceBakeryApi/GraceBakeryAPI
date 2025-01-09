package cohort46.gracebakeryapi.bakery.product.dao;

import cohort46.gracebakeryapi.bakery.category.model.Category;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Stream<Product> findProductsByIsActive(Boolean isactive);
    //Stream<Product> findProductsByCategory_Id(Long category_id, Sort sort);
    List<Product> findProductsByCategoryId(Long category_id, Sort sort) ;
    List<Product> findProductsByCategoryIdAndIsActive(Long category_id, Boolean isactive, Sort sort);
}


