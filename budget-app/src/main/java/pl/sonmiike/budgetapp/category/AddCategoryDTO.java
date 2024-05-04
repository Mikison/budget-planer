package pl.sonmiike.budgetapp.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryDTO {

    @NotBlank(message = "Name cannot be blank")
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 32, message = "Name must be between 2 and 32 characters")
    private String name;


    private String iconUrl;

}
