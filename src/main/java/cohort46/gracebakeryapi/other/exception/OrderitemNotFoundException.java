package cohort46.gracebakeryapi.other.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderitemNotFoundException extends RuntimeException {
    public OrderitemNotFoundException(Long id) {
        super("Order item with ID " + id + " not found");
    }
}