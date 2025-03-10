package cohort46.gracebakeryapi.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorizationRespondDto {
    Long id;
    String token;
}
