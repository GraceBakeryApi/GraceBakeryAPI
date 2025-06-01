package cohort46.gracebakeryapi.other.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OAuth2EmailNotFoundException extends RuntimeException {
    public OAuth2EmailNotFoundException() {
        super("Email not found in OAuth2 provider response");
    }
}