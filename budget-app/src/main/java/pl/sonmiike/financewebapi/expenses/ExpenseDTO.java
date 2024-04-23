package pl.sonmiike.financewebapi.expenses;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDTO {

    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date;
    @NotBlank
    @DecimalMin(value = "0.0", message = "Amount must be greater than 0", inclusive = false)
    private String amount;

    private Long userId;
    @NotNull
    private Long categoryId;

}
