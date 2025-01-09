package cohort46.gracebakeryapi.bakery.order.dao;

import cohort46.gracebakeryapi.bakery.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.stream.Stream;


public interface OrderRepository extends JpaRepository<Order, Long> {
    Stream<Order> findOrdersByUserId(Long userId, Sort sort);
}


