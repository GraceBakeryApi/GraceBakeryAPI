package cohort46.gracebakeryapi.bakery.orderitem.dao;

import cohort46.gracebakeryapi.bakery.orderitem.model.Orderitem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface OrderitemRepository extends JpaRepository<Orderitem, Long> {
    Stream<Orderitem> findOrderitemsByOrderId(Long orderId, Sort sort);
    Stream<Orderitem> findOrderitemsByProductId(Long productId, Sort sort);

}

