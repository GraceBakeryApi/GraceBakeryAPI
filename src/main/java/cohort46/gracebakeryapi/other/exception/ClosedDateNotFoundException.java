package cohort46.gracebakeryapi.other.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClosedDateNotFoundException extends RuntimeException {
    public ClosedDateNotFoundException(Long id) {
        super("Address with ID " + id + " not found");
    }
}