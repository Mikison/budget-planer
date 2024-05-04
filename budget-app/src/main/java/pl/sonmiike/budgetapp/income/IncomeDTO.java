package pl.sonmiike.budgetapp.income;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeDTO {

    @NotNull
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Date cannot be null")
    private LocalDate incomeDate;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 32, message = "Name must be between 21 and 32 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 100, message = "Description can have max 100 characters")
    private String description;

    @DecimalMin(value = "0.00", inclusive = false)
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    private Long userId;
}
