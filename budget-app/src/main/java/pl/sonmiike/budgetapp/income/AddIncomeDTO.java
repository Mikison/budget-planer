package pl.sonmiike.budgetapp.income;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddIncomeDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate incomeDate;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 32, message = "Name must be between 1 and 32 characters")
    private String name;

    @Size(max = 100, message = "Description can have max 100 characters")
    private String description;
    @NotNull
    @DecimalMin(value = "0.0", message = "Amount must be greater than 0", inclusive = false)
    private BigDecimal amount;
}
