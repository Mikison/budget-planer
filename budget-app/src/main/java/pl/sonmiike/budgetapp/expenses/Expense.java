package pl.sonmiike.budgetapp.expenses;


import jakarta.persistence.*;
import lombok.*;
import pl.sonmiike.budgetapp.category.Category;
import pl.sonmiike.budgetapp.user.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate date;
    private BigDecimal amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne( optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    void update(ExpenseDTO updateData) {
        if (updateData.getName() != null) this.name = updateData.getName();

        if (updateData.getDescription() != null) this.description = updateData.getDescription();

        if (updateData.getDate() != null) this.date = updateData.getDate();

        if (updateData.getAmount() != null) this.amount = updateData.getAmount();
    }
}
