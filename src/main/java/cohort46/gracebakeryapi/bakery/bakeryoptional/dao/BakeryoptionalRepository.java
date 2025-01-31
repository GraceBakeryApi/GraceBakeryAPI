package cohort46.gracebakeryapi.bakery.bakeryoptional.dao;

import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.category.model.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface BakeryoptionalRepository extends JpaRepository<Bakeryoptional, Long> {
    Stream<Bakeryoptional>  findByProductsId(Long product_id, Sort sort);
}