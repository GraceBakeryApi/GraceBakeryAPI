package cohort46.gracebakeryapi.helperclasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@Getter
@Setter
public class SizePrice {
    @NotNull
    private Long sizeid;
    private String title_de;
    private String title_ru;
    private Double mass;
    private Integer diameter;
    private Integer persons;
    @NotNull
    private Double price;
}
