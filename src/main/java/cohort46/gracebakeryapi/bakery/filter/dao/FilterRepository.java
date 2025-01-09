package cohort46.gracebakeryapi.bakery.filter.dao;

import cohort46.gracebakeryapi.bakery.filter.model.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface FilterRepository extends JpaRepository<Filter, Long> {
    Stream<Filter> findByProductsId(Long product_id);
}