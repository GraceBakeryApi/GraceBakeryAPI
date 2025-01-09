package cohort46.gracebakeryapi.bakery.image.dao;

import cohort46.gracebakeryapi.bakery.image.model.Image;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Stream<Image> findByProductId(Long productId, Sort sort);
}