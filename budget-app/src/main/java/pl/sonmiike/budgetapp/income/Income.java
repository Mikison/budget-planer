package pl.sonmiike.budgetapp.income;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sonmiike.budgetapp.user.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate incomeDate;

    private String name;

    private String description;
    private BigDecimal amount;

    @ManyToOne
    private UserEntity user;


    void update(IncomeDTO updatedData) {
        if (updatedData.getName() != null) this.name = updatedData.getName();

        if (updatedData.getDescription() != null) this.description = updatedData.getDescription();

        if (updatedData.getIncomeDate() != null) this.incomeDate = updatedData.getIncomeDate();

        if (updatedData.getAmount() != null) this.amount = updatedData.getAmount();
    }
}
