package cohort46.gracebakeryapi.other.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException {
    public UserExistsException(Long id) {
        super("UserAccount with ID " + id + " is already exists");
    }
}