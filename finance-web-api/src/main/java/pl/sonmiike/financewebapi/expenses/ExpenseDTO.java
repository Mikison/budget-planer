package pl.sonmiike.financewebapi.expenses;

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

    @NotBlank
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date;
    @NotBlank
    private String amount;
    @NotBlank
    private Long userId;
    @NotBlank
    private Long categoryId;

}
