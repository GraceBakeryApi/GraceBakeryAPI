package cohort46.gracebakeryapi.accounting.dto;

import lombok.Getter;

@Getter
public class ChangePasswordDto {
    String oldPassword;
    String newPassword;
}
