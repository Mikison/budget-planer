package pl.sonmiike.budgetapp.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
public class CategoryDTO {

    @NotNull(message = "Id cannot be null")
    private Long id;
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 32, message = "Name must be between 2 and 32 characters")
    private String name;

    @NotNull
    private BigDecimal budget;


}
