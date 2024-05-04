package pl.sonmiike.budgetapp.expenses;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddExpenseDTO {

    @NotBlank(message = "Name cannot be blank")
    @NotNull(message = "Name cannot be null")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 100, message = "Description can have max 100 characters")
    private String description;

    @NotNull(message = "Date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.0", message = "Amount must be greater than 0", inclusive = false)
    private BigDecimal amount;
}
