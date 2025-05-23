package cohort46.gracebakeryapi.order.order.dao;

import cohort46.gracebakeryapi.order.address.model.Address;
import cohort46.gracebakeryapi.order.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;


public interface OrderRepository extends JpaRepository<Order, Long> {
    Stream<Order> findOrdersByUserId(Long userId, Sort sort);
    Stream<Order> findOrdersByAddress(Address address, Sort sort);
}


