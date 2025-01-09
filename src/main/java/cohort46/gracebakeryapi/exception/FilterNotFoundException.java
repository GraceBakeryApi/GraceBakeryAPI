package cohort46.gracebakeryapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FilterNotFoundException extends RuntimeException {
    public FilterNotFoundException(Long id) {
        super("Filter with ID " + id + " not found");
    }
}