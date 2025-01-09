package cohort46.gracebakeryapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SectionNotFoundException extends RuntimeException {
    public SectionNotFoundException(Long id) {
        super("Section with ID " + id + " not found");
    }
}