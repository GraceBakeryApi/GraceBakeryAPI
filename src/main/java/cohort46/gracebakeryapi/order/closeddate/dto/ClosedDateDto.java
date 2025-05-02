package cohort46.gracebakeryapi.order.closeddate.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
public class ClosedDateDto {
    @NotNull
    @Setter
    private Long id;
    private Long date;
    private String comment;
}