package cohort46.gracebakeryapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SizeNotFoundException extends RuntimeException {
    public SizeNotFoundException(Long id) {
        super("Size with ID " + id + " not found");
    }
}