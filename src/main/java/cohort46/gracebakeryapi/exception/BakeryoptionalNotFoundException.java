package cohort46.gracebakeryapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BakeryoptionalNotFoundException extends RuntimeException {
    public BakeryoptionalNotFoundException(Long id) {
        super("Option with ID " + id + " not found");
    }
}