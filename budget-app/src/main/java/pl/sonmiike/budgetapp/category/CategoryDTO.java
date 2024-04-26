package pl.sonmiike.budgetapp.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
public class CategoryDTO {

    @NotNull
    private Long id;
    @NotBlank
    private String name;

    @NotBlank
    private BigDecimal budget;


}
