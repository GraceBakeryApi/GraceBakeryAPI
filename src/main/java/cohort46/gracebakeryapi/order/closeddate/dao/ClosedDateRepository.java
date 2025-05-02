package cohort46.gracebakeryapi.order.closeddate.dao;

import cohort46.gracebakeryapi.order.closeddate.model.ClosedDate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ClosedDateRepository extends JpaRepository<ClosedDate, Long> {
}